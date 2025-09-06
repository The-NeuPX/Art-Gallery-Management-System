import java.text.DecimalFormat;

public class EliteVisitor extends ArtGalleryVisitor {
    // Additional attributes for EliteVisitor
    private boolean assignedPersonalArtAdvisor; // Tracks if visitor has a personal art advisor
    private boolean exclusiveEventAccess;       // Tracks if visitor has access to exclusive events

    // Constructor
    public EliteVisitor(int visitorId, String fullName, String gender, String contactNumber, 
                       String registrationDate, String ticketType) {
        // Call parent class constructor
        super(visitorId, fullName, gender, contactNumber, registrationDate, ticketType);
        
        // Initialize EliteVisitor attributes
        this.assignedPersonalArtAdvisor = false;
        this.exclusiveEventAccess = false;
    }

    // Getter methods for EliteVisitor attributes
    public boolean getAssignedPersonalArtAdvisor() {
        return assignedPersonalArtAdvisor;
    }

    public boolean getExclusiveEventAccess() {
        return exclusiveEventAccess;
    }

    // Method to assign personal art advisor
    public boolean assignPersonalArtAdvisor() {
        // Check if reward points exceed 5000
        if (rewardPoints > 5000) {
            assignedPersonalArtAdvisor = true;
        }
        return assignedPersonalArtAdvisor;
    }

    // Method to check exclusive event access
    public boolean exclusiveEventAccess() {
        // If personal art advisor is assigned, grant exclusive event access
        if (assignedPersonalArtAdvisor) {
            exclusiveEventAccess = true;
        }
        return exclusiveEventAccess;
    }

    // Override buyProduct method
    @Override
    public String buyProduct(String artworkName, double artworkPrice) {
        // Check if visitor is active
        if (!isActive) {
            return "Please log in to make a purchase.";
        }
        
        // Check if no purchase or different artwork
        if (!isBought || this.artworkName.equals("")) {
            this.artworkName = artworkName;
            this.artworkPrice = artworkPrice;
            this.isBought = true;
            this.buyCount += 1;
            return "Purchase successful: " + artworkName;
        }
        
        // Check if trying to buy the same artwork
        if (this.artworkName.equals(artworkName)) {
            return "You already purchased this artwork: " + artworkName;
        }
        
        return "Purchase failed.";
    }

    // Override calculateDiscount method
    @Override
    public double calculateDiscount() {
        // Check if product is bought
        if (!isBought) {
            return 0.0;
        }
        
        // Calculate 40% discount
        discountAmount = artworkPrice * 0.40;
        finalPrice = artworkPrice - discountAmount;
        return discountAmount;
    }

    // Override calculateRewardPoint method
    @Override
    public double calculateRewardPoint() {
        // Check if product is bought
        if (!isBought) {
            return 0.0;
        }
        
        // Calculate reward points (10 points per rupee of final price)
        rewardPoints += finalPrice * 10;
        return rewardPoints;
    }

    // Override generateBill method
    @Override
    public void generateBill() {
        if (!isBought) {
            System.out.println("No purchase made to generate bill.");
            return;
        }
        
        // Format numbers to 2 decimal places
        DecimalFormat df = new DecimalFormat("#.00");
        
        System.out.println("=== Bill ===");
        System.out.println("Visitor ID: " + visitorId);
        System.out.println("Visitor Name: " + fullName);
        System.out.println("Artwork Name: " + artworkName);
        System.out.println("Artwork Price: " + df.format(artworkPrice));
        System.out.println("Discount Amount: " + df.format(discountAmount));
        System.out.println("Final Price: " + df.format(finalPrice));
        System.out.println("=============");
    }

    // Private method to terminate visitor account
    private void terminateVisitor() {
        isActive = false;
        assignedPersonalArtAdvisor = false;
        exclusiveEventAccess = false;
        visitCount = 0;
        cancelCount = 0;
        rewardPoints = 0.0;
    }

    // Override cancelProduct method
    @Override
    public String cancelProduct(String artworkName, String cancellationReason) {
        // Check if cancel limit is reached
        if (cancelCount >= cancelLimit) {
            terminateVisitor();
            return "Account terminated due to too many cancellations.";
        }
        
        // Check if no purchase was made
        if (buyCount == 0) {
            return "No product to cancel.";
        }
        
        // Check if artwork names match
        if (!this.artworkName.equals(artworkName)) {
            return "Incorrect artwork name.";
        }
        
        // Process cancellation
        this.cancellationReason = cancellationReason;
        refundableAmount = artworkPrice - (artworkPrice * 0.05); // 5% cancellation fee
        rewardPoints -= finalPrice * 10; // Remove reward points
        this.artworkName = "";
        this.artworkPrice = 0.0;
        this.isBought = false;
        cancelCount += 1;
        buyCount -= 1;
        finalPrice = 0.0;
        discountAmount = 0.0;
        
        return "Cancellation successful. Refund: " + refundableAmount;
    }

    // Override display method
    @Override
    public void display() {
        // Call parent class display method
        super.display();
        
        // Display EliteVisitor attributes
        System.out.println("Assigned Personal Art Advisor: " + assignedPersonalArtAdvisor);
        System.out.println("Exclusive Event Access: " + exclusiveEventAccess);
        
    }
}