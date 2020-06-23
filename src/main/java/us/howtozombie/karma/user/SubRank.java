package us.howtozombie.karma.user;

import java.util.ArrayList;
import java.util.HashMap;

public class SubRank {

    public static HashMap<Integer, SubRank> subranks = new HashMap<>();
    public static int rankNum = 0;

    private int id;
    private String tag;
    private String shortTag;

    private int maxNum;

    private boolean canRankup;
    private int rankup;
    private int rankupTo;

    private boolean canRankdown;
    private int rankdown;
    private int rankdownTo;

    private int permissions;

    private boolean subcategories;
    private ArrayList<Integer> subs = new ArrayList<>();

    public SubRank(int id, String tag, String shortTag, boolean canRankup, int rankup, int rankupTo, boolean canRankdown, int rankdown,
                   int rankdownTo, int permissions, boolean subcategories, ArrayList<Integer> subs) {

        this.id = id;
        this.tag = tag;
        this.shortTag = shortTag;
        this.canRankup = canRankup;
        this.rankup = rankup;
        this.rankupTo = rankupTo;
        this.canRankdown = canRankdown;
        this.rankdown = rankdown;
        this.rankdownTo = rankdownTo;
        this.permissions = permissions;
        this.subcategories = subcategories;
        this.subs = subs;
        this.maxNum = this.subs.size();

        subranks.put(id, this);

    }

    public static HashMap<Integer, SubRank> getSubRanks() {
        return subranks;
    }

    public int getId() {
        return id;
    }

    public Integer getMaxNum() {
        return maxNum;
    }

    public Integer getNum(int i) {
        if (i == 0) {
            return -1;
        }
        i -= 1;
        return subs.get(i);
    }

    public int getPermissions() {
        return permissions;
    }

    public int getRankdown() {
        return rankdown;
    }

    public int getRankdownTo() {
        return rankdownTo;
    }

    public boolean canRankdown() {
        return canRankdown;
    }

    public boolean canRankup() {
        return canRankup;
    }

    public int getRankup() {
        return rankup;
    }

    public int getRankupTo() {
        return rankupTo;
    }

    public String getShortTag() {
        return shortTag;
    }

    public String getTag() {
        return tag;
    }

    public boolean hasSubcategories() {
        return subcategories;
    }
}
