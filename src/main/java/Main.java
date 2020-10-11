import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main (String[] args) {
        try {
            ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();

            ArrayList<String> names = new ArrayList<String>();
            char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();


            System.setProperty("webdriver.chrome.driver", "/Users/rubengonzalez/Downloads/chromedriver_2");
            WebDriver driver = new ChromeDriver();
            WebDriverWait wait = new WebDriverWait(driver, 1800);
            Actions actions = new Actions(driver);

            for(int j = 0; j<alphabet.length;j++) {
                driver.get("https://www.fhnw.ch/de/die-fhnw/hochschulen/hsw/media-newsroom/"+alphabet[j]+"_alle-mitarbeitenden-der-hochschule-fuer-wirtschaft-fhnw");
                Thread.sleep(2000);
                List<WebElement> dozentNamen = driver.findElements(By.cssSelector(".widg_contact__name>a>span"));
                List<WebElement> dozentBeschreibung = driver.findElements(By.cssSelector(".widg_contact__name>span"));
                List<WebElement> dozentContact = driver.findElements(By.cssSelector(".widg_contact__text>a"));
                List<WebElement> dozentLinks = driver.findElements(By.cssSelector(".widg_contact__name>a"));
                List<WebElement> dozentPictures = driver.findElements(By.cssSelector(".widg_contact__portrait__img>a>img"));

                ArrayList<String> contactList = new ArrayList<String>();
                ArrayList<String> phoneList = new ArrayList<String>();


                for (int i = 0; i< dozentContact.size();i++){
                    String contact = (dozentContact.get(i).getAttribute("innerHTML"));
                    if(!contact.startsWith("+")){
                        contactList.add(contact);
                    } else{
                        phoneList.add(contact);
                    }
                }


                    Thread.sleep(1000);
                System.out.println("Dozent Namen: "+dozentNamen.size());
                System.out.println("Beschreibungen: "+dozentBeschreibung.size());
                System.out.println("emails: "+contactList.size());


                for (int i = 0; i < dozentNamen.size(); i++) {
                    String name = dozentNamen.get(i).getAttribute("innerHTML");
                    if (!(name.startsWith("Telefon") || name.startsWith("E-Mail"))) {
                        if(dozentBeschreibung.get(i).getAttribute("innerHTML").contains("Dozent")||dozentBeschreibung.get(i).getAttribute("innerHTML").contains("Leiter")) {

                            String description = "\""+dozentBeschreibung.get(i).getAttribute("innerHTML")+"\"";
                            String mail = contactList.get(i);
                            String link = dozentLinks.get(i).getAttribute("href");
                            String pictureUrl = dozentPictures.get(i).getAttribute("src");

                            String phone = "";
                            if(phoneList.size()== contactList.size()){
                                phone = phoneList.get(i);
                            }
                            String title = "";
                            if(name.contains(".")){
                                title = name.substring(0,name.lastIndexOf(".")+1);
                                name = name.substring(name.lastIndexOf(".")+2);
                            }
                            ArrayList<String> entry = new ArrayList<String>();

                            System.out.println(pictureUrl);
                            System.out.println(name);
                            System.out.println(title);
                            System.out.println(description);
                            System.out.println(mail);
                            System.out.println(phone);
                            System.out.println(link);
                            System.out.println("Internal");
                            entry.add(pictureUrl);
                            entry.add(name);
                            entry.add(title);
                            entry.add(description);
                            entry.add("");
                            entry.add(mail);
                            entry.add(phone);
                            entry.add(link);
                            entry.add("Internal");

                            rows.add(entry);

                            names.add(name);


                        }
                    }
                }
                Thread.sleep(1000);
            }

            try {
                FileWriter writer = new FileWriter(new File("/Users/rubengonzalez/IdeaProjects/moodleScript/src/main/java/output.csv"));

                writer.append("Picture");
                writer.append(',');
                writer.append("Name");
                writer.append(',');
                writer.append("Title");
                writer.append(',');
                writer.append("Position");
                writer.append(',');
                writer.append("Fields of Study");
                writer.append(',');
                writer.append("Mail");
                writer.append(',');
                writer.append("Phone");
                writer.append(',');
                writer.append("Information");
                writer.append(',');
                writer.append("External/Internal");
                writer.append('\n');

                for (ArrayList<String> rowData : rows) {
                    writer.append(String.join(",", rowData));
                    writer.append("\n");
                }

                writer.flush();
                writer.close();


                System.out.println("done!");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();

            }


            System.out.println("total lecturers: "+ names.size());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
