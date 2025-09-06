/**
 * @author (Bibek Neupane)
 */


public abstract class ArtGalleryVisitor 
{
    // Ticket types and costs
    protected final String STANDARD_TICKET = "Standard";
    protected final String ELITE_TICKET = "Elite";
    protected final double STANDARD_COST = 1000.0;
    protected final double ELITE_COST = 2000.0;
    protected final int CANCEL_LIMIT = 3; // Fixed cancel limit

    // All attributes
    protected int visitorId;
    protected String fullName;
    protected String gender;
    protected String contactNumber;
    protected String registrationDate;
    protected double ticketCost;
    protected String ticketType;
    protected int visitCount;
    protected double rewardPoints;
    protected final int cancelLimit;
    protected int cancelCount;
    protected String cancellationReason;
    protected double refundableAmount;
    protected boolean isActive;
    protected boolean isBought;
    protected int buyCount;
    protected double finalPrice;
    protected double discountAmount;
    protected String artworkName;
    protected double artworkPrice;

    // Constructor to set up the visitor
    public ArtGalleryVisitor(int visitorId, String fullName, String gender, String contactNumber, 
                             String registrationDate, String ticketType) {
        this.visitorId = visitorId;
        this.fullName = fullName;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.registrationDate = registrationDate;
        
        // Set ticket type and cost
        if (ticketType.equals(STANDARD_TICKET)) {
            this.ticketType = STANDARD_TICKET;
            this.ticketCost = STANDARD_COST;
        } else if (ticketType.equals(ELITE_TICKET)) {
            this.ticketType = ELITE_TICKET;
            this.ticketCost = ELITE_COST;
        } else {
            this.ticketType = "";
            this.ticketCost = 0.0;
        }

        // Initialize other attributes
        this.visitCount = 0;
        this.rewardPoints = 0.0;
        this.cancelLimit = CANCEL_LIMIT;
        this.cancelCount = 0;
        this.cancellationReason = "";
        this.refundableAmount = 0.0;
        this.isActive = false;
        this.isBought = false;
        this.buyCount = 0;
        this.finalPrice = 0.0;
        this.discountAmount = 0.0;
        this.artworkName = "";
        this.artworkPrice = 0.0;
    }

    // Getter methods for all attributes
    public int getVisitorId() {
        return visitorId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getGender() {
        return gender;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public double getTicketCost() {
        return ticketCost;
    }

    public String getTicketType() {
        return ticketType;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public double getRewardPoints() {
        return rewardPoints;
    }

    public int getCancelLimit() {
        return cancelLimit;
    }

    public int getCancelCount() {
        return cancelCount;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public double getRefundableAmount() {
        return refundableAmount;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public boolean getIsBought() {
        return isBought;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public String getArtworkName() {
        return artworkName;
    }

    public double getArtworkPrice() {
        return artworkPrice;
    }

    // Setter methods for fullName, gender, contactNumber
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    // Method to log a visit
    public void logVisit() {
        visitCount = visitCount + 1;
        isActive = true;
    }

    // Abstract methods to be implemented by child classes
    public abstract String buyProduct(String artworkName, double artworkPrice);
    public abstract double calculateDiscount();
    public abstract double calculateRewardPoint();
    public abstract String cancelProduct(String artworkName, String cancellationReason);
    public abstract void generateBill();

    // Method to display visitor details
    public void display() {
        System.out.println("Visitor Details:");
        System.out.println("ID: " + visitorId);
        System.out.println("Name: " + fullName);
        System.out.println("Gender: " + gender);
        System.out.println("Contact: " + contactNumber);
        System.out.println("Registration Date: " + registrationDate);
        System.out.println("Ticket Type: " + ticketType);
        System.out.println("Ticket Cost: " + ticketCost);
        System.out.println("Visit Count: " + visitCount);
        System.out.println("Reward Points: " + rewardPoints);
        System.out.println("Cancel Limit: " + cancelLimit);
        System.out.println("Cancel Count: " + cancelCount);
        System.out.println("Cancellation Reason: " + cancellationReason);
        System.out.println("Refundable Amount: " + refundableAmount);
        System.out.println("Is Active: " + isActive);
        System.out.println("Has Bought: " + isBought);
        System.out.println("Buy Count: " + buyCount);
        System.out.println("Final Price: " + finalPrice);
        System.out.println("Discount Amount: " + discountAmount);
        System.out.println("Artwork Name: " + artworkName);
        System.out.println("Artwork Price: " + artworkPrice);
        
    }
}
