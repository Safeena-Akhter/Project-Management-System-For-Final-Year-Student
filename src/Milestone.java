public class Milestone {
    private String id;
    private String title;
    private Date dueDate;
    private String description;

   public void updateMilestone(String title, Date dueDate, String description) {
    this.title = title;
    this.dueDate = dueDate;
    this.description = description;
   }
}