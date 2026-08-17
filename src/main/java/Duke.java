import java.util.Scanner;

public class Duke {
    public static void main(String[] args) {
        String separator = "____________________________________________________________";
        String banner = "+-------+\n"
                + "|  yqr  |\n"
                + "+-------+";

        System.out.println(separator);
        System.out.println(banner);
        System.out.println("Hello! I'm yqr.");
        System.out.println("What can I do for you?");
        System.out.println(separator);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                System.out.println(separator);
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(separator);
                break;
            }

            System.out.println(separator);
            System.out.println(command);
            System.out.println(separator);
        }
    }
}
