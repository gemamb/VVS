package es.udc.pa.pa001.apuestas.testautomation;

import static es.udc.pa.pa001.apuestas.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa001.apuestas.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

import org.graphwalker.core.machine.ExecutionContext;
import org.graphwalker.java.annotation.GraphWalker;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa001.apuestas.model.category.Category;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pa.pa001.apuestas.model.event.EventDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE,
    SPRING_CONFIG_TEST_FILE })
@Transactional
@GraphWalker(value = "random(edge_coverage(100))", start = "save")
public class StatesImplTest extends ExecutionContext implements StatesTest{

  public final static Path MODEL_PATH = Paths.get("es/udc/pa/pa001/apuestas/testautomation/States.graphml");
  
  @Autowired
  private EventDao eventDao;
  
  Calendar eventCalendar = Calendar.getInstance();
  Category category =  new Category("Baloncesto");
  Event event = new Event("Real Madrid - Barcelona", eventCalendar, category);
  
  boolean creado=false;
  boolean eliminado=false;
  
  @Override
  public void Eliminado() {
    if (eliminado)
      System.out.println("Evento creado");
    else
      System.out.println("Evento no creado");
  }

  @Override
  public void find(){    
    try {
      assertEquals(event, eventDao.find(event.getEventId()));
      System.out.println("Evento encontrado");
    } catch (InstanceNotFoundException e) {
      System.out.println("Evento no encontrado");
    }
  }

  @Override
  public void save() {
    eventCalendar = Calendar.getInstance();
    category =  new Category("Baloncesto");
    event = new Event("Real Madrid - Barcelona", eventCalendar, category);
    eventDao.save(event);
    creado = true;
    System.out.println("Evento creado");
  }

  @Override
  public void Creado() {
    if (creado)
      System.out.println("Evento creado");
    else
      System.out.println("Evento no creado");
  }

  @Override
  public void remove() {
    
    try {
      eventDao.remove(event.getEventId());
      System.out.println("Evento eliminado");
      eliminado=true;
    } catch (InstanceNotFoundException e) {
      System.out.println("Evento no eliminado");
      eliminado=false;
    }
  }
}
