package fr.eni.ecole.enchereseniprojetbackend.DTO.request;

public class MoneyRequest {

    private Long id;
    private Long money;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }
}
