package model;

public class CardAddress {
    private Place place;
    private Owner owner;
    private int number;

    public CardAddress(Place place, Owner owner, int number) {
        this.place = place;
        this.owner = owner;
        this.number = number;
    }

    public CardAddress(Place place, Owner owner) {
        this.place = place;
        this.owner = owner;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
