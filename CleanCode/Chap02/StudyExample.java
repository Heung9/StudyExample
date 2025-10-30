package CleanCode.Chap02;

public class StudyExample {


    public static void main(String[] args) {
        example1_IntentionRevealing();
        example2_AvoidMisleadingInfo();
        example3_ConsistentVocabulary();
        example4_PronounceableAndSearchable();
        example5_NoEncodingPrefix();
        example6_NounAndVerbNaming();
        example7_AddContext();
        example8_RemoveUnnecessaryContext();
        example9_ClarityOverCleverness();
    }

    // 1. 의도를 분명히 하라
    private static void example1_IntentionRevealing() {
        System.out.println("===== 1. 의도를 분명히 하라 =====");
        // ❌ 잘못된 예시
        int d = 5; // d가 뭘 의미하는지 모름
        System.out.println("Bad: d = " + d);

        // ✅ 수정된 예시
        int elapsedTimeInDays = 5; // 명확한 의미
        System.out.println("Good: elapsedTimeInDays = " + elapsedTimeInDays);
    }

    // 2. 그릇된 정보와 혼동을 피하라
    private static void example2_AvoidMisleadingInfo() {
        System.out.println("\n===== 2. 그릇된 정보와 혼동을 피하라 =====");
        // ❌ 잘못된 예시
        String accountList = "user123"; // 실제로는 리스트가 아닌데 이름이 list
        System.out.println("Bad: accountList = " + accountList);

        // ✅ 수정된 예시
        String accountId = "user123"; // 실제 의도 반영
        System.out.println("Good: accountId = " + accountId);
    }

    // 3. 의미 있게 구분하고 일관된 어휘를 유지하라
    private static void example3_ConsistentVocabulary() {
        System.out.println("\n===== 3. 의미 있게 구분하고 일관된 어휘를 유지하라 =====");
        // ❌ 잘못된 예시
        String data1 = getUserData();
        String data2 = fetchCustomerData();
        String data3 = retrieveClientData();
        System.out.println("Bad: " + data1 + ", " + data2 + ", " + data3);

        // ✅ 수정된 예시
        String userData = fetchUserData();
        String customerData = fetchCustomerDataUnified();
        System.out.println("Good: " + userData + ", " + customerData);
    }

    private static String getUserData() { return "getUserData()"; }
    private static String fetchCustomerData() { return "fetchCustomerData()"; }
    private static String retrieveClientData() { return "retrieveClientData()"; }
    private static String fetchUserData() { return "fetchUserData()"; }
    private static String fetchCustomerDataUnified() { return "fetchCustomerDataUnified()"; }

    // 4. 발음하기 쉽고 검색 가능한 이름을 써라
    private static void example4_PronounceableAndSearchable() {
        System.out.println("\n===== 4. 발음하기 쉽고 검색 가능한 이름을 써라 =====");
        // ❌ 잘못된 예시
        long genymdhms = System.currentTimeMillis();
        System.out.println("Bad: genymdhms = " + genymdhms);

        // ✅ 수정된 예시
        long generationTimestamp = System.currentTimeMillis();
        System.out.println("Good: generationTimestamp = " + generationTimestamp);
    }

    // 5. 인코딩과 불필요한 접두어를 피하라
    private static void example5_NoEncodingPrefix() {
        System.out.println("\n===== 5. 인코딩과 불필요한 접두어를 피하라 =====");
        // ❌ 잘못된 예시 (헝가리 표기법)
        int m_nUserCount = 10;
        System.out.println("Bad: m_nUserCount = " + m_nUserCount);

        // ✅ 수정된 예시
        int userCount = 10;
        System.out.println("Good: userCount = " + userCount);
    }

    // 6. 명사와 동사의 구분
    private static void example6_NounAndVerbNaming() {
        System.out.println("\n===== 6. 명사와 동사의 구분 =====");
        // ❌ 잘못된 예시
        CustomerBad c1 = new CustomerBad("홍길동");
        c1.customer(); // 메서드 이름이 명사형
        // ✅ 수정된 예시
        CustomerGood c2 = new CustomerGood("홍길동");
        c2.save(); // 동사형으로 수정
    }

    static class CustomerBad {
        private final String name;
        public CustomerBad(String name) { this.name = name; }
        public void customer() {
            System.out.println("Bad: customer() called for " + name);
        }
    }

    static class CustomerGood {
        private final String name;
        public CustomerGood(String name) { this.name = name; }
        public void save() {
            System.out.println("Good: save() called for " + name);
        }
    }

    // 7. 맥락을 부여하라
    private static void example7_AddContext() {
        System.out.println("\n===== 7. 맥락을 부여하라 =====");
        // ❌ 잘못된 예시
        String state = "서울"; // 어느 지역의 state인지 불명확
        System.out.println("Bad: state = " + state);

        // ✅ 수정된 예시
        Address address = new Address("서울", "강남구", "12345");
        System.out.println("Good: Address.state = " + address.getState());
    }

    // 8. 불필요한 맥락 제거
    private static void example8_RemoveUnnecessaryContext() {
        System.out.println("\n===== 8. 불필요한 맥락 제거 =====");
        // ❌ 잘못된 예시
        GSDAccountAddress badAddress = new GSDAccountAddress("부산", "해운대구", "54321");
        System.out.println("Bad: " + badAddress.getCity());

        // ✅ 수정된 예시
        Address goodAddress = new Address("부산", "해운대구", "54321");
        System.out.println("Good: " + goodAddress.getCity());
    }

    static class GSDAccountAddress {
        private final String city;
        private final String district;
        private final String zipCode;
        public GSDAccountAddress(String city, String district, String zipCode) {
            this.city = city;
            this.district = district;
            this.zipCode = zipCode;
        }
        public String getCity() { return city; }
    }

    static class Address {
        private final String state;
        private final String city;
        private final String zipCode;
        public Address(String state, String city, String zipCode) {
            this.state = state;
            this.city = city;
            this.zipCode = zipCode;
        }
        public String getState() { return state; }
        public String getCity() { return city; }
    }

    // 9. 기발함보다 명료함을 택하라
    private static void example9_ClarityOverCleverness() {
        System.out.println("\n===== 9. 기발함보다 명료함을 택하라 =====");
        // ❌ 잘못된 예시
        holyHandGrenade();

        // ✅ 수정된 예시
        deleteItems();
    }

    private static void holyHandGrenade() {
        System.out.println("Bad: holyHandGrenade() → 유머러스하지만 의미 불명확");
    }

    private static void deleteItems() {
        System.out.println("Good: deleteItems() → 명확히 항목 삭제를 의미");
    }

}
