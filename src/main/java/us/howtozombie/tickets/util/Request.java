package us.howtozombie.tickets.util;

import org.bukkit.Location;
import us.howtozombie.Main;
import us.howtozombie.karma.user.User;

import java.util.*;

public class Request {

    public static HashMap<Integer, Request> requests = new HashMap<>();

    public static HashMap<Integer, Request> reviewRequests = new HashMap<>();

    public static HashMap<Integer, Request> artRequests = new HashMap<>();
    public static HashMap<Integer, Request> buildRequests = new HashMap<>();
    public static HashMap<Integer, Request> cmdRequests = new HashMap<>();
    public static HashMap<Integer, Request> devRequests = new HashMap<>();
    public static HashMap<Integer, Request> gmRequests = new HashMap<>();
    public static HashMap<Integer, Request> managerRequests = new HashMap<>();

    public static HashMap<Integer, Request> lowRequests = new HashMap<>();
    public static HashMap<Integer, Request> medRequests = new HashMap<>();
    public static HashMap<Integer, Request> highRequests = new HashMap<>();

    public static HashMap<Integer, Request> groupRequests = new HashMap<>();

    public static HashMap<Integer, Request> pinnedRequests = new HashMap<>();

    private int id;
    private RequestType type;
    private String description;
    private int urgency;
    private Date date;
    private User creator;
    private ArrayList<UUID> claim = new ArrayList<>();
    private User reviewer;
    private RequestStage stage;
    private Location loc;
    private String comments;
    private boolean group;
    private boolean pinned;

    public Request(int id, String type, String stage, String description, int urgency, Date date, User creator, ArrayList<UUID> claim, User reviewer, Location loc, String comments, boolean group, boolean pinned) {

        this.id = id;
        this.description = description;
        this.urgency = urgency;
        this.date = date;
        this.creator = creator;
        if (claim != null) {
            this.claim = claim;
        }
        this.stage = RequestStage.fromString(stage);
        this.reviewer = reviewer;
        this.loc = loc;
        this.comments = comments;
        this.group = group;
        this.pinned = pinned;

        requests.put(id, this);
        if (type.equalsIgnoreCase("artist")) {
            artRequests.put(id, this);
            this.type = RequestType.ARTIST;
        } else if (type.equalsIgnoreCase("builder")) {
            buildRequests.put(id, this);
            this.type = RequestType.BUILDER;
        } else if (type.equalsIgnoreCase("cmd")) {
            cmdRequests.put(id, this);
            this.type = RequestType.CMD;
        } else if (type.equalsIgnoreCase("developer")) {
            devRequests.put(id, this);
            this.type = RequestType.DEVELOPER;
        } else if (type.equalsIgnoreCase("gm")) {
            gmRequests.put(id, this);
            this.type = RequestType.GM;
        } else if (type.equalsIgnoreCase("manager")) {
            managerRequests.put(id, this);
            this.type = RequestType.MANAGER;
        }

        if (urgency == 1) {
            lowRequests.put(id, this);
        } else if (urgency == 2) {
            medRequests.put(id, this);
        } else if (urgency == 3) {
            highRequests.put(id, this);
        }

        if (this.stage.equals(RequestStage.REVIEW)) {
            reviewRequests.put(id, this);
        }

        if (this.group) {
            groupRequests.put(id, this);
        }

        if (this.pinned) {
            pinnedRequests.put(id, this);
        }

    }

    public static void createNewRequest(int id, String type, String description, int urgency, User creator, Location loc) {

        Request r = new Request(id, type, "UNCLAIMED", description, urgency, new Date(), creator, null, null, loc, null, false, false);
        Main.updateRequest(r);

    }

    public static void createNewRequest(int id, String type, String description, int urgency, User creator, Location loc, boolean group) {

        Request r = new Request(id, type,"UNCLAIMED", description, urgency, new Date(), creator, null, null, loc, null, group, false);
        Main.updateRequest(r);

    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
        if (group) {
            groupRequests.put(id, this);
        } else {
            groupRequests.remove(id);
        }
    }

    public boolean isPinned() {
        return group;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
        if (pinned) {
            pinnedRequests.put(id, this);
        } else {
            pinnedRequests.remove(id);
        }
    }

    public int getId() {
        return id;
    }

    public RequestType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        Main.updateRequest(this);
    }

    public int getUrgency() {
        return urgency;
    }

    public void setUrgency(int urgency) {
        this.urgency = urgency;
        Main.updateRequest(this);
    }

    public Date getDate() {
        return date;
    }

    public User getCreator() {
        return creator;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
        Main.updateRequest(this);
    }

    public ArrayList<UUID> getClaim() {
        return claim;
    }

    public void setClaim(UUID claim) {
        this.claim.clear();
        this.claim.add(claim);
        Main.updateRequest(this);
    }

    public void addClaim(UUID claim) {
        this.claim.add(claim);
        Main.updateRequest(this);
    }

    public void removeClaim(UUID claim) {
        this.claim.remove(claim);
        Main.updateRequest(this);
    }

    public void clearClaim() {
        this.claim.clear();
        Main.updateRequest(this);
    }

    public RequestStage getStage() {
        return stage;
    }

    public void setStage(RequestStage stage) {
        this.stage = stage;
        Main.updateRequest(this);
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
        Main.updateRequest(this);
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
        Main.updateRequest(this);
    }

    public enum RequestType {
        ARTIST("&5", "Artist"), BUILDER("&a", "Builder"), CMD("&3", "CMD"), DEVELOPER("&4", "Developer"), GM("&b", "GM"), MANAGER("&c", "Manager");

        public String color;
        public String name;

        RequestType(String color, String name) {
            this.color = color;
            this.name = name;
        }

        public String getName() {

            return name;

        }

        public boolean hasRank(User u) {

            if (this.equals(RequestType.ARTIST)) {
                return u.getArtRank() > 1;
            } else if (this.equals(RequestType.BUILDER)) {
                return u.getBuildRank() > 1;
            } else if (this.equals(RequestType.CMD)) {
                return u.getCmdRank() > 1;
            } else if (this.equals(RequestType.DEVELOPER)) {
                return u.getDevRank() > 1;
            } else if (this.equals(RequestType.GM)) {
                return u.getGMRank() > 1;
            } else if (this.equals(RequestType.MANAGER)) {
                return u.getAdminRank() > 1;
            }

            return false;

        }

        public static RequestType fromString(String s) {

            if (s.equalsIgnoreCase("art")) {
                return RequestType.ARTIST;
            } else if (s.equalsIgnoreCase("build")) {
                return RequestType.BUILDER;
            } else if (s.equalsIgnoreCase("cmd")) {
                return RequestType.CMD;
            } else if (s.equalsIgnoreCase("dev")) {
                return RequestType.DEVELOPER;
            } else if (s.equalsIgnoreCase("manager")) {
                return RequestType.MANAGER;
            } else if (s.equalsIgnoreCase("gm")) {
                return RequestType.GM;
            }
            return RequestType.ARTIST;
        }
    }

    public enum RequestStage {
        UNCLAIMED, CLAIMED, REVIEW, DONE;

        public static RequestStage fromString(String s) {

            if (s.equalsIgnoreCase("unclaimed")) {
                return RequestStage.UNCLAIMED;
            } else if (s.equalsIgnoreCase("claimed")) {
                return RequestStage.CLAIMED;
            } else if (s.equalsIgnoreCase("review")) {
                return RequestStage.REVIEW;
            } else if (s.equalsIgnoreCase("done")) {
                return RequestStage.DONE;
            }
            return RequestStage.UNCLAIMED;
        }
    }

}
