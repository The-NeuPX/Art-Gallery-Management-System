public class StandardVisitor extends ArtGalleryVisitor {
    // Additional attributes for StandardVisitor
    private boolean isEligibleForDiscountUpgrade; // Tracks if visitor qualifies for higher discount
    private final int visitLimit;                // Fixed limit for visits to upgrade discount
    private float discountPercent;              // Discount percentage (e.g., 0.10 for 10%)

    // Constructor
    public StandardVisitor(int visitorId, String fullName, String gender, String contactNumber, 
                           String registrationDate, String ticketType) {
        // Call parent class constructor
        super(visitorId, fullName, gender, contactNumber, registrationDate, ticketType);
        
        // Initialize StandardVisitor attributes
        this.visitLimit = 5;                    // Set visit limit to 5
        this.discountPercent = 0.10f;           // Set initial discount to 10%
        this.isEligibleForDiscountUpgrade = false; // Not eligible for upgrade initially
    }

    // Getter methods for StandardVisitor attributes
    public boolean getIsEligibleForDiscountUpgrade() {
        return isEligibleForDiscountUpgrade;
    }

    public int getVisitLimit() {
        return visitLimit;
    }

    public float getDiscountPercent() {
        return discountPercent;
    }

    // Check if visitor qualifies for discount upgrade
    public boolean checkDiscountUpgrade() {
        if (visitCount >= visitLimit) {
            isEligibleForDiscountUpgrade = true;
            discountPercent = 0.15f; // Upgrade discount to 15%
        }
        return isEligibleForDiscountUpgrade;
    }

    // Buy a product (override from parent)
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
            this.buyCount = buyCount + 1;
            return "Purchase successful: " + artworkName;
        }
        
        // Check if trying to buy the same artwork
        if (this.artworkName.equals(artworkName)) {
            return "You already purchased this artwork: " + artworkName;
        }
        
        return "Purchase failed.";
    }

    // Calculate discount (override from parent)
    public double calculateDiscount() {
        // Check if product is bought
        if (!isBought) {
            return 0.0;
        }
        
        // Check for discount upgrade
        checkDiscountUpgrade();
        
        // Calculate discount
        discountAmount = artworkPrice * discountPercent;
        finalPrice = artworkPrice - discountAmount;
        return discountAmount;
    }

    // Calculate reward points (override from parent)
    public double calculateRewardPoint() {
        // Check if product is bought
        if (!isBought) {
            return 0.0;
        }
        
        // Calculate reward points (5 points per rupee of final price)
        rewardPoints += finalPrice * 5;
        return rewardPoints;
    }

    // Generate bill (override from parent)
    public void generateBill() {
        if (!isBought) {
            System.out.println("No purchase made to generate bill.");
            return;
        }
        
        System.out.println("=== Bill ===");
        System.out.println("Visitor ID: " + visitorId);
        System.out.println("Visitor Name: " + fullName);
        System.out.println("Artwork Name: " + artworkName);
        System.out.println("Artwork Price: " + artworkPrice);
        System.out.println("Discount Amount: " + discountAmount);
        System.out.println("Final Price: " + finalPrice);
        
    }

    // Terminate visitor account (private method)
    private void terminateVisitor() {
        isActive = false;
        isEligibleForDiscountUpgrade = false;
        visitCount = 0;
        cancelCount = 0;
        rewardPoints = 0.0;
    }

    // Cancel a product (override from parent )
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
        refundableAmount = artworkPrice - (artworkPrice * 0.10); // 10% cancellation fee
        rewardPoints -= finalPrice * 5; // Remove reward points
        this.artworkName = "";
        this.artworkPrice = 0.0;
        this.isBought = false;
        cancelCount = cancelCount + 1;
        buyCount = buyCount - 1;
        finalPrice = 0.0;
        discountAmount = 0.0;
        
        return "Cancellation successful. Refund: " + refundableAmount;
    }

    // Display details (override from parent)
    public void display() {
        // Call parent class display method
        super.display();
        
        // Display StandardVisitor attributes
        System.out.println("Eligible for Discount Upgrade: " + isEligibleForDiscountUpgrade);
        System.out.println("Visit Limit: " + visitLimit);
        System.out.println("Discount Percent: " + (discountPercent * 100) + "%");
    }
}