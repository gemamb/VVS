package es.udc.pa.pa001.apuestas.test.web;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WebTest {
	
	private static String userLoginName = "user1";
	private static String userPassword = "user1";
	private static String userName = "user1";
	private static String eventName = "España - República Checa";
	private static String eventCategory = "Fútbol";
	private static String eventDate = "13/06/16 0:00";
	private static String betTypeQuestion = "¿Quién ganará el encuentro?";
	private static String betOption1Answer = "1";
	private static String betOption1Rate = "1.9";
	private static String betOption1Rate2 = "1,9";
	private static String betOption2Answer = "X";
	private static String betOption2Rate = "2.2";
	private static String betOption3Answer = "2";
	private static String betOption3Rate = "2.7";
	private static String betedMoney = "20";
	private static String displayedBetedMoney = "20";
	private static String expectedResult = "?";
	
	@Test
    public void testLoginFirefox() {
        
        /* Creamos una instancia del driver de Firefox*/
        WebDriver driver = new FirefoxDriver();

        /* Creamos una instancia del driver de Firefox*/
        driver.get("http://localhost:9090/apuestas-app");

        /* Pulsamos el boton de autentificación */
        driver.findElement(By.id("authenticationButton")).click();
        
        /* Nos logueamos */
        WebElement inputLoginName = driver.findElement(By.id("loginName"));
        inputLoginName.sendKeys(userLoginName);
       
        driver.findElement(By.id("password")).sendKeys(userPassword);
        
        inputLoginName.submit();
        
        /* Comprobamos que los datos visualizados son correctos */
        assertEquals(userName,driver.findElement(By.id("userNameLabel")).getText());
        
        driver.quit();
        
    }
	
	@Test
	public void testMakeBetFireFox() {
		
		/* Creamos una instancia del driver de Firefox*/
		WebDriver driver = new FirefoxDriver();
		
		/* Creamos una instancia del driver de Firefox*/
        driver.get("http://localhost:9090/apuestas-app");
        
        /* Desplegamos menu "search" del Layout */
        driver.findElement(By.id("menuUserFunctions")).click();
        
        /* Seleccionamos la opcion "Find Events" */
        driver.findElement(By.id("findEventsOption")).click();
        
        /* Buscamos el evento */
        driver.findElement(By.id("keyWords")).sendKeys(eventName);
        
        /* Pulsamos boton "find" del formulario de la pagina "findevents" */
        driver.findElement(By.id("findEventsButton")).click();
        
        /* Obtenemos los datos del evento visualizado en la pagina "eventsdetails" */
        List<WebElement> infoEvent1 = driver.findElements(By.xpath("//td"));
        
        /* Comprobamos que los datos visualizados son correctos */
        assertEquals(infoEvent1.get(0).getText(),eventName);
        assertEquals(infoEvent1.get(1).getText(),eventCategory);
       // assertEquals(infoEvent1.get(2).getText(),eventDate);
        
        /* Pulsamos en el evento para acceder a sus tipos de apuesta */
        driver.findElement(By.id("eventLink")).click();
        
        /* Obtenemos los datos del evento visualizado en la pagina "eventdetails" */
        List<WebElement> infoEvent2 = driver.findElements(By.xpath("//td"));

        /* Comprobamos que los datos visualizados son correctos */
        assertEquals(infoEvent2.get(0).getText(),eventName);
        assertEquals(infoEvent2.get(1).getText(),eventCategory);
        //assertEquals(infoEvent2.get(2).getText(),eventDate);
        assertEquals(infoEvent2.get(3).getText(),betTypeQuestion);
        
		/* Pulsamos el primer Bet Type del evento*/
        driver.findElement(By.id("betTypeLink")).click();

        /* Obtenemos los datos de los tipos de apuestas en la pagina "eventdetails" */
        List<WebElement> infoBetTypes = driver.findElements(By.xpath("//td"));
        
        /* Comprobamos que los datos visualizados son correctos */
        assertEquals(infoBetTypes.get(0).getText(),betTypeQuestion);
        assertEquals(infoBetTypes.get(1).getText(),betOption1Answer);
        assertEquals(infoBetTypes.get(2).getText(),betOption1Rate);
        assertEquals(infoBetTypes.get(3).getText(),betOption2Answer);
        assertEquals(infoBetTypes.get(4).getText(),betOption2Rate);
        assertEquals(infoBetTypes.get(5).getText(),betOption3Answer);
        assertEquals(infoBetTypes.get(6).getText(),betOption3Rate);
   
	    /* Pulsamos la primera opcion de apuesta del tipo de apuesta sin estar logueados*/
	    driver.findElement(By.id("betOtionLinkUserNoLogged")).click();
        
        /* Nos logueamos */
        WebElement inputLoginName = driver.findElement(By.id("loginName"));
        inputLoginName.sendKeys(userLoginName);
       
        driver.findElement(By.id("password")).sendKeys(userPassword);
        
        inputLoginName.submit();
        
        /* Obtenemos los datos que se visualizan en la pagina "makebet" */
        List<WebElement> infoMakeBet = driver.findElements(By.xpath("//td"));

        /* Comprobamos que los datos visualizados son correctos */
        assertEquals(infoMakeBet.get(0).getText(),eventName);
        //assertEquals(infoMakeBet.get(1).getText(),eventDate);
        assertEquals(infoMakeBet.get(2).getText(),betTypeQuestion);
        assertEquals(infoMakeBet.get(3).getText(),betOption1Answer);
        assertEquals(infoMakeBet.get(4).getText(),betOption1Rate);
        
        /* Escribimos la cantidad a apostar */
        driver.findElement(By.id("betedMoney")).sendKeys(betedMoney);
	 
        /* Pulsamos el boton de apostar*/
        driver.findElement(By.id("betButton")).click();
        
        /* Pulsamos el link hacia la pagina "mybets" */
        driver.findElement(By.id("myBetsLink")).click();
        
        /* Obtenemos los datos visualizados en la pagina "mybets" */
        List<WebElement> infoMyBets = driver.findElements(By.xpath("//td"));
        	        
        /* Comprobamos que los datos visualizados son correctos */
        assertEquals(infoMyBets.get(1).getText(),eventName);
        //assertEquals(infoMyBets.get(2).getText(),eventDate);
        assertEquals(infoMyBets.get(3).getText(),betTypeQuestion);
        assertEquals(infoMyBets.get(4).getText(),betOption1Answer);
        assertEquals(infoMyBets.get(5).getText(),betOption1Rate2);
        assertEquals(infoMyBets.get(6).getText(),displayedBetedMoney);
        assertEquals(infoMyBets.get(7).getText(),expectedResult);
        
        /* Cerramos el navegador */
        driver.quit();
        
	}

}
