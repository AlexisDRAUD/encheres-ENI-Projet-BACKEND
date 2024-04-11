package fr.eni.ecole.enchereseniprojetbackend.DTO.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchFilterInput {

    private Long userId;

    private String search;

    private Long categorieId;

    private boolean openBids;

    private boolean ongoingBids;

    private boolean wonBids;

    private boolean ongoingSales;

    private boolean notStartedSales;

    private boolean completedSales;

    public int countTrueBooleans() {
        int count = 0;
        if (openBids) count++;
        if (ongoingBids) count++;
        if (wonBids) count++;
        if (ongoingSales) count++;
        if (notStartedSales) count++;
        if (completedSales) count++;
        return count;
    }

}
