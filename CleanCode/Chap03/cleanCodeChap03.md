# Clean Code Refactoring Files

## Main.java
```java
package CleanCode.Chap03;

import java.util.ArrayList;

public class Main {public static void main(String[] args) {
    OrderCheckout svc = new OrderCheckout();

    // 정상 케이스
    System.out.println("RECEIPT=" + svc.checkout("vip001 , A-100 , 2 , COUPON10"));

    // 입력 에러
    try {
        svc.checkout(" ,A-100,-1,");
    } catch (Exception e) {
        System.out.println("ERROR=" + e.getMessage());
    }
}
}

```

## OrderCheckout.java
```java
package CleanCode.Chap03;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Clean Code 기반 리팩터링 버전
 * - 함수는 작게, 한 가지 일만 수행
 * - 의미 있는 이름 사용
 * - 플래그/출력 인자 제거
 * - 예외 기반 오류 처리
 * - 부수 효과 제거
 */

/* ==============================
   1. 작고 한 가지 일만 하는 함수로 분해
      checkout() 메서드는 “비즈니스 플로우(흐름)”만 담당하고,
      나머지 세부 절차는 모두 작고 명확한 함수로 나뉘었습니다.

   2. 한 수준의 추상화만 유지
      checkout() 함수는 “이야기처럼 읽히는 흐름”으로 개선되었고,
      구체적 수식 계산은 별도 정책 객체로 위임되어 추상화 수준이 통일되었습니다.

   3. 플래그·출력 인자 제거
      출력 인자 제거,
      불필요한 플래그 제거,
      대신 CheckoutRequest DTO로 모든 입력을 묶어 전달합니다.

   4. 오류는 예외로 처리 (오류코드 문자열 제거)
      오류 로직이 단순하고 예측 가능하게 바뀌었으며,
      checkout() 함수는 “성공하거나 예외 던지거나” 두 가지 상태만 가집니다.

   5. 커맨드-쿼리 분리 & 부수효과 제거
      checkout()은 오직 “명령(Command)” 역할,
      검증/계산은 “조회(Query)” 역할로 분리되어
      부수효과가 완전히 제거되었습니다.

   6. 매직 넘버/문자열 상수화 & 정책 캡슐화
      정책 변경이 필요한 경우 PricePolicy / DiscountPolicy만 수정하면 됩니다.


    OrderCheckout 클래스는 파싱, 검증, 계산, 할인, 저장 로직을 분리하여 각 기능을 수행하는 작은 함수들로 재구성.
    불필요한 플래그와 출력 인자를 제거하고 불변 DTO로 입력 데이터를 명확히 전달.
    오류 처리는 문자열 반환 대신 예외로 전환하여 흐름 제어를 단순화.
    매직 넘버를 상수화하고 정책 클래스로 캡슐화하여 변경에 유연하도록 처리.
   ============================== */
public class OrderCheckout {
    private final PricePolicy pricePolicy = new PricePolicy();
    private final DiscountPolicy discountPolicy = new DiscountPolicy();
    private final OrderRepository orderRepository = new OrderRepository();

    /**
     * 주문 체크아웃 처리
     */

    public String checkout(String rawInput) {
        CheckoutRequest request = CheckoutParser.parse(rawInput);
        validateRequest(request);
        ensureStockAvailable(request);

        BigDecimal total = pricePolicy.calculateTotal(request.itemId(), request.quantity());
        BigDecimal discounted = discountPolicy.applyDiscount(total, request.isVip(), request.couponCode());

        String orderId = orderRepository.save(request.userId(), request.itemId(), request.quantity(), discounted);
        return new Receipt(orderId, request.userId(), request.itemId(), request.quantity(), discounted).toString();
    }

    private void validateRequest(CheckoutRequest req) {
        if (req.userId() == null || req.userId().isBlank()) {
            throw new ValidationException("USER_REQUIRED");
        }
        if (req.itemId() == null || req.itemId().isBlank()) {
            throw new ValidationException("ITEM_REQUIRED");
        }
        if (req.quantity() <= 0) {
            throw new ValidationException("QTY_POSITIVE");
        }
    }

    private void ensureStockAvailable(CheckoutRequest req) {
        if (req.quantity() > 20) {
            throw new OutOfStockException("OUT_OF_STOCK");
        }
    }
}

/* ==============================
   요청/결과
   ============================== */
record CheckoutRequest(String userId, String itemId, int quantity, boolean isVip, String couponCode) {}

record Receipt(String orderId, String userId, String itemId, int quantity, BigDecimal totalAmount) {
    @Override
    public String toString() {
        return String.format("OK:%s:%s:%s:%d:%s", orderId, userId, itemId, quantity, totalAmount);
    }
}

/* ==============================
   입력 문자열 파싱
   ============================== */
class CheckoutParser {
    public static CheckoutRequest parse(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new ValidationException("EMPTY_INPUT");
        }

        String[] parts = raw.split(",");
        if (parts.length < 3) {
            throw new ValidationException("INVALID_FORMAT");
        }

        String userId = parts[0].trim();
        String itemId = parts[1].trim();
        String qtyStr = parts[2].trim();
        String coupon = (parts.length >= 4) ? parts[3].trim() : "";

        int qty;
        try {
            qty = Integer.parseInt(qtyStr);
        } catch (NumberFormatException ex) {
            throw new ValidationException("QTY_NOT_NUMBER");
        }

        boolean vip = userId.startsWith("vip"); // 단순 규칙 예시
        return new CheckoutRequest(userId, itemId, qty, vip, coupon);
    }
}

/* ==============================
   가격/할인 정책
   ============================== */
class PricePolicy {
    private static final int BASE_A = 100;
    private static final int BASE_B = 150;
    private static final int SHIPPING_A = 500;
    private static final int SHIPPING_B = 1200;
    private static final int WEIGHT_PER_UNIT = 2;
    private static final int EXTRA_WEIGHT_THRESHOLD = 10;
    private static final int EXTRA_FEE = 800;

    public BigDecimal calculateTotal(String itemId, int quantity) {
        int baseUnit = itemId.startsWith("A") ? BASE_A : BASE_B;
        int shipping = itemId.startsWith("A") ? SHIPPING_A : SHIPPING_B;
        int weight = WEIGHT_PER_UNIT * quantity;
        int extra = (weight > EXTRA_WEIGHT_THRESHOLD) ? EXTRA_FEE : 0;

        return BigDecimal.valueOf(baseUnit)
                .multiply(BigDecimal.valueOf(quantity))
                .add(BigDecimal.valueOf(shipping))
                .add(BigDecimal.valueOf(extra));
    }
}

class DiscountPolicy {
    public BigDecimal applyDiscount(BigDecimal price, boolean isVip, String couponCode) {
        BigDecimal discounted = price;
        if (isVip) {
            discounted = discounted.multiply(BigDecimal.valueOf(0.9)); // 10% VIP 할인
        }
        if ("COUPON10".equalsIgnoreCase(couponCode)) {
            discounted = discounted.subtract(BigDecimal.TEN); // 10원 차감
        }
        return discounted.max(BigDecimal.ZERO);
    }
}

/* ==============================
   저장 리포지토리 (Mock)
   ============================== */
class OrderRepository {
    public String save(String userId, String itemId, int quantity, BigDecimal totalAmount) {
        String orderId = UUID.randomUUID().toString();
        System.out.printf("SAVE: order=%s user=%s item=%s qty=%d sum=%s%n",
                orderId, userId, itemId, quantity, totalAmount);
        return orderId;
    }
}

/* ==============================
   예외 정의
   ============================== */
class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}

class OutOfStockException extends RuntimeException {
    public OutOfStockException(String message) {
        super(message);
    }
}


```

## MainRefTest.java
```java
package CleanCode.Chap03;

/**
 * 리팩터링된 OrderCheckout.java 기능 검증용
 * - 정상 시나리오
 * - 검증 실패 시나리오
 * - 재고 부족 시나리오
 */
public class MainRefTest {

    public static void main(String[] args) {
        var svc = new OrderCheckout();

        System.out.println("=== [1] 정상 시나리오 ===");
        try {
            String receipt = svc.checkout("vip001 , A-100 , 2 , COUPON10");
            System.out.println("RECEIPT=" + receipt);
        } catch (Exception e) {
            System.out.println("FAIL: 예외 발생 → " + e.getMessage());
        }

        System.out.println("\n=== [2] 검증 실패 시나리오 ===");
        try {
            svc.checkout(" , A-100 , -1 , ");
            System.out.println("FAIL: 예외가 발생해야 함");
        } catch (ValidationException e) {
            System.out.println("PASS: ValidationException 발생 → " + e.getMessage());
        } catch (Exception e) {
            System.out.println("FAIL: 잘못된 예외 유형 → " + e.getClass().getSimpleName());
        }

        System.out.println("\n=== [3] 재고 부족 시나리오 ===");
        try {
            svc.checkout("user01 , B-200 , 99 , ");
            System.out.println("FAIL: 예외가 발생해야 함");
        } catch (OutOfStockException e) {
            System.out.println("PASS: OutOfStockException 발생 → " + e.getMessage());
        } catch (Exception e) {
            System.out.println("FAIL: 잘못된 예외 유형 → " + e.getClass().getSimpleName());
        }

        System.out.println("\n=== [4] 포맷 오류 시나리오 ===");
        try {
            svc.checkout("user01,");
            System.out.println("FAIL: 예외가 발생해야 함");
        } catch (ValidationException e) {
            System.out.println("PASS: ValidationException 발생 → " + e.getMessage());
        }

        System.out.println("\n=== [5] 비VIP 사용자 + 쿠폰 없음 ===");
        try {
            String receipt = svc.checkout("userX , B-300 , 3 , ");
            System.out.println("RECEIPT=" + receipt);
        } catch (Exception e) {
            System.out.println("FAIL: 예외 발생 → " + e.getMessage());
        }

        System.out.println("\n=== 테스트 종료 ===");
    }
}

```
