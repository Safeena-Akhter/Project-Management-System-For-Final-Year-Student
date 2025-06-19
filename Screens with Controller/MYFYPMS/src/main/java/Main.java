public class Main
 {
    public static void main(String[] args) {
        ProjectController controller = new ProjectController();
        

        // Show screens (you can enable/disable these as needed)
       new RegisterProjectScreen(controller).setVisible(true);
       new ApproveProjectScreen(controller).setVisible(true);
       new ReceiveFeedbackScreen(controller).setVisible(true); // <-- New feedback screen
    }
}




