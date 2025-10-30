
# 📘 의미 있는 이름 (Clean Code Chapter 2) ---

## 1️⃣ 의도를 분명히 하라

```java
// ❌ 의미가 모호한 변수명과 메서드 호출
List<Account> a = accountRepository.findAllByCid(cid);

// ✅ 의도가 명확한 변수명과 파라미터
List<Account> accountList = accountRepository.findAllByCustomerId(customerId);
```

---

## 2️⃣ 그릇된 정보와 혼동을 피하라

```java
// ❌ 단일 객체인데 리스트처럼 보이는 이름
Optional<Branch> branchList = branchRepository.findById(branchId);

// ✅ 단일 객체임을 명확히 표현
Optional<Branch> branchInfo = branchRepository.findById(branchId);
```

---

## 3️⃣ 의미 있게 구분하고 일관된 어휘를 유지하라

```java
// ❌ 동일한 기능인데 메서드 이름이 혼용됨
accountRepository.fetchByBzn(bzn);
accountRepository.retrieveByBzn(bzn);

// ✅ 일관된 어휘 사용 (JPA 표준 findBy)
accountRepository.findByBzn(bzn);
```

---

## 4️⃣ 발음하기 쉽고 검색 가능한 이름을 써라

```java
// ❌ 약어 남용, 의미 불명확
String bAccCrDt = "2025-10-30";

// ✅ 명확하고 발음 가능한 이름
String businessAccountCreatedDate = "2025-10-30";
```

---

## 5️⃣ 인코딩과 불필요한 접두어를 피하라

```java
// ❌ 헝가리식 표기법
String m_strBznName = "IBK기업은행";

// ✅ 타입은 IDE가 제공하므로 접두어 불필요
String bankName = "IBK기업은행";
```

---

## 6️⃣ 명사와 동사의 구분

```java
// ❌ 명사형 메서드 (행동이 불분명)
accountRepository.account();

// ✅ 동사형 메서드 (행위가 명확)
accountRepository.openAccount();
```

---

## 7️⃣ 맥락을 부여하라

```java
// ❌ 맥락이 없는 필드명
String id = "001";

// ✅ 상위 구조(엔티티)로 맥락 제공
branch.setBranchId("001");
branch.setBranchName("서울 강남지점");
```

---

## 8️⃣ 불필요한 맥락 제거

```java
// ❌ 중복된 맥락 포함
class IBKBankAccountRepository { ... }

// ✅ 이미 프로젝트가 은행 도메인임을 감안
class AccountRepository { ... }
```

---

## 9️⃣ 기발함보다 명료함을 택하라

```java
// ❌ 지나치게 창의적인 이름
transactionService.unleashMoneyBeast(customerId);

// ✅ 명확하고 실무적인 이름
transactionService.withdrawFunds(customerId);
```

---
