
# 📘 의미 있는 이름 (Clean Code Chapter 2) — 은행 도메인 JPA 예제

이 문서는 **클린 코드 2장 "의미 있는 이름"** 원칙을  
**은행 업무 도메인 (Account, Branch, Bzn, Deposit 등)**  
기반의 **JPA 예제 코드**로 설명합니다.  

각 항목은 ❌ 잘못된 예시 / ✅ 올바른 예시 형태로 구성되어 있습니다.

---

## 1️⃣ 의도를 분명히 하라

```java
// ❌ 의미가 모호한 변수명과 메서드 호출
List<Account> a = accountRepository.findAllByCid(cid);

// ✅ 의도가 명확한 변수명과 파라미터
List<Account> accountList = accountRepository.findAllByCustomerId(customerId);
```

> 🔹 변수명과 파라미터 이름만으로 코드의 의도를 파악할 수 있도록 명명하세요.

---

## 2️⃣ 그릇된 정보와 혼동을 피하라

```java
// ❌ 단일 객체인데 리스트처럼 보이는 이름
Optional<Branch> branchList = branchRepository.findById(branchId);

// ✅ 단일 객체임을 명확히 표현
Optional<Branch> branchInfo = branchRepository.findById(branchId);
```

> 🔹 `List`, `Map`, `Set` 등의 접미사는 컬렉션이 아닐 경우 사용하지 않습니다.

---

## 3️⃣ 의미 있게 구분하고 일관된 어휘를 유지하라

```java
// ❌ 동일한 기능인데 메서드 이름이 혼용됨
accountRepository.fetchByBzn(bzn);
accountRepository.retrieveByBzn(bzn);

// ✅ 일관된 어휘 사용 (JPA 표준 findBy)
accountRepository.findByBzn(bzn);
```

> 🔹 프로젝트 전반에서 동일한 개념은 동일한 용어로,  
> 다른 개념은 확실히 구분된 단어로 표현하세요.

---

## 4️⃣ 발음하기 쉽고 검색 가능한 이름을 써라

```java
// ❌ 약어 남용, 의미 불명확
String bAccCrDt = "2025-10-30";

// ✅ 명확하고 발음 가능한 이름
String businessAccountCreatedDate = "2025-10-30";
```

> 🔹 발음 가능한 단어를 사용하면 협업 중 구두로 소통하기 쉽고,  
> IDE 검색에서도 높은 효율을 가집니다.

---

## 5️⃣ 인코딩과 불필요한 접두어를 피하라

```java
// ❌ 헝가리식 표기법
String m_strBznName = "IBK기업은행";

// ✅ 타입은 IDE가 제공하므로 접두어 불필요
String bankName = "IBK기업은행";
```

> 🔹 현대 IDE 환경에서는 `m_`, `str`, `i` 등의 접두어가 불필요합니다.

---

## 6️⃣ 명사와 동사의 구분

```java
// ❌ 명사형 메서드 (행동이 불분명)
accountRepository.account();

// ✅ 동사형 메서드 (행위가 명확)
accountRepository.openAccount();
```

> 🔹 클래스나 엔티티는 **명사형**,  
> 메서드는 **동사형**으로 작성하여 역할이 명확히 구분되도록 합니다.

---

## 7️⃣ 맥락을 부여하라

```java
// ❌ 맥락이 없는 필드명
String id = "001";

// ✅ 상위 구조(엔티티)로 맥락 제공
branch.setBranchId("001");
branch.setBranchName("서울 강남지점");
```

> 🔹 맥락이 필요한 경우 상위 객체나 네이밍으로 의미를 명확히 표현하세요.

---

## 8️⃣ 불필요한 맥락 제거

```java
// ❌ 중복된 맥락 포함
class IBKBankAccountRepository { ... }

// ✅ 이미 프로젝트가 은행 도메인임을 감안
class AccountRepository { ... }
```

> 🔹 패키지명, 프로젝트명에서 이미 맥락이 드러난다면  
> 클래스명에 중복으로 포함하지 마세요.

---

## 9️⃣ 기발함보다 명료함을 택하라

```java
// ❌ 지나치게 창의적인 이름
transactionService.unleashMoneyBeast(customerId);

// ✅ 명확하고 실무적인 이름
transactionService.withdrawFunds(customerId);
```

> 🔹 코드는 **유머나 은유가 아닌 실용문**입니다.  
> 읽는 사람이 즉시 이해할 수 있는 이름을 선택하세요.

---

## 🧾 정리 요약표

| 번호 | 원칙 | 잘못된 예시 | 올바른 예시 |
|------|------|--------------|--------------|
| 1 | 의도를 분명히 | `List<Account> a` | `List<Account> accountList` |
| 2 | 혼동 피하기 | `branchList` | `branchInfo` |
| 3 | 일관된 어휘 | `fetch/retrieve` | `findBy` |
| 4 | 발음/검색 용이 | `bAccCrDt` | `businessAccountCreatedDate` |
| 5 | 인코딩 제거 | `m_strBznName` | `bankName` |
| 6 | 명사/동사 구분 | `account()` | `openAccount()` |
| 7 | 맥락 부여 | `id` | `branch.branchId` |
| 8 | 불필요한 맥락 제거 | `IBKBankAccountRepository` | `AccountRepository` |
| 9 | 명료함 우선 | `unleashMoneyBeast()` | `withdrawFunds()` |

---

### 🏦 프로젝트 가정
- Spring Boot + JPA 기반  
- 도메인: 은행(Account, Branch, Transaction, Bzn 등)  
- Repository 네이밍은 Spring Data JPA 표준(`findBy`, `existsBy`, `deleteBy`) 사용  

---

### 🧠 참고
> 출처: 로버트 C. 마틴 『Clean Code』 2장 — 의미 있는 이름  
> 적용 도메인 예제: IBK 기업은행 (Banking System)

---

### 🪄 추천 사용 방법
- `/docs/naming/MeaningfulNames.md` 형태로 저장 후  
- 개발 컨벤션 문서(예: `CONTRIBUTING.md`, `CODESTYLE.md`)에 링크하세요.  
- 신규 개발자 온보딩 및 코드 리뷰 시 참고 자료로 활용 가능합니다.

---

### 📂 파일 이름 제안
`MeaningfulNames_Banking_JPA.md`  
또는  
`docs/naming/clean-code-meaningful-names.md`
