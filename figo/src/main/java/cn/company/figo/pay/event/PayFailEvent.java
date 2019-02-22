package cn.company.figo.pay.event;

public class PayFailEvent {
    public String info = "支付失败";

    public PayFailEvent(String info) {
        this.info = info;
    }

    public PayFailEvent() {
    }
}
