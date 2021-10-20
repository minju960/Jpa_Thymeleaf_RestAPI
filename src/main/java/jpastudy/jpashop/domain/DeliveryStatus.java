package jpastudy.jpashop.domain;

// ex) 요일을 1~7로 설정하면 데이터 형식이 int 이므로 1~7이 아닌 다른 숫자가 들어오면 컴파일타임에서 오류를 못잡아낸다.
public enum DeliveryStatus {
    READY, COMP
}
