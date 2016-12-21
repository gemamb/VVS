package es.udc.pa.pa001.apuestas.testautomation;

import org.graphwalker.java.annotation.Model;
import org.graphwalker.java.annotation.Vertex;

import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

import org.graphwalker.java.annotation.Edge;

@Model(file = "es/udc/pa/pa001/apuestas/testautomation/States.graphml")
public interface StatesTest {

 @Vertex()
 void Eliminado();

 @Edge()
 void find();

 @Edge()
 void save();

 @Vertex()
 void Creado();

 @Edge()
 void remove();
}
