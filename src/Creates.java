import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import java.util.Scanner;

/**
 * Created by 53638138e on 13/02/17.
 */
public class Creates {
    private static ObjectContainer db = null;
    public static void main(String[]args){

        Scanner scr = new Scanner(System.in);
        int opcion = 0;
       while(opcion != 6){
            //Try catch para evitar que el programa termine si hay un error
            try{

                System.out.println("--------------------------------------------");
                System.out.println("1 -> Crear datos : ");
                System.out.println("2 -> Crear Personas Random : ");
                System.out.println("3 -> Consultar datos : ");
                System.out.println("4 -> Crear Brian nuevo : ");
                System.out.println("5 -> Conprueba si una persona existe antes de insertarla en la base de datos : ");
                System.out.println("6 -> Salir : ");
                System.out.println("---------------------------------------");
                opcion = scr.nextInt();
                switch (opcion) {
                    case 1:
                        Crear();//Crear una persona con los datos que introduces por pantalla
                        break;
                    case 2:
                        PersonasAutomaticas();//Creas las personas predeterminadas en el ej
                        break;
                    case 3:
                        Consulta();//Busca en la base de datos la persona que quieras introduciendo por nombre
                        break;
                    case 4:
                        InsertarNuevoBrian();//Inserta un nuevo objeto Brian
                        break;
                    case 5:
                        PreguntaAntesDeInsertar(); //Pregunta antes de crear una persona si existe
                        break;
                    default:
                        break;
                }
                System.out.println("Operacion realizada con exito");
            }catch(Exception e){
                System.out.println("Uoop! Error!");
            }
        }
    }

    public static void Crear(){
        try
        {
            Scanner scr = new Scanner(System.in);
            //Archivo
            db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "persons.data");

            //Preguntamos los datos que queremos insertar
            System.out.println("Dime el nombre de la Persona : ");
            String name = scr.next();
            System.out.println("Dime el apellido de la Persona : ");
            String lastName = scr.next();
            System.out.println("Dime la edat de la Persona : ");
            int edad = scr.nextInt();
            // Metemos el objeto en la base de datos
            Person nombre = new Person (name,lastName,edad);
            //Guardamos
            db.store(nombre);
            db.commit();

        }
        finally {
            if (db != null)
                db.close();
        }
    }

    public static void PersonasAutomaticas(){
        //Archivo
        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "persons.data");
        try {
            //Datos predefinidos por mi
            Person brian = new Person("Brian", "Goetz", 39);
            Person jason = new Person("Jason", "Hunter", 35);
            Person brians = new Person("Brian", "Sletten", 38);
            Person david = new Person("David", "Geary", 55);
            Person glenn = new Person("Glenn", "Vanderberg", 40);
            Person neal = new Person("Neal", "Ford", 39);

            //Metemos los objetos en el file
            db.store(brian);
            db.store(jason);
            db.store(brians);
            db.store(david);
            db.store(glenn);
            db.store(neal);
            //Guardamos
            db.commit();

        } finally {
            if (db != null)
                db.close();
        }
    }

    public static void Consulta(){
        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "persons.data");
        try {

            Scanner teclat = new Scanner(System.in);
            //Preguntamos el nombre que queremos buscar
            System.out.println("Dime el nombre de la Persona que quieres buscar ");
            String name = teclat.next();

            //BUscame todas las personas con ese nombre
            ObjectSet<Person> set = db.queryByExample(new Person(name, null, 0));

            //Imprimimos
            while(set.hasNext())
                System.out.println(set.next());

        }
        finally {
            if (db != null)
                db.close();
        }
    }

    public static void InsertarNuevoBrian(){

        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "persons.data");
        try {
            //Creamos nuevo Brian
            Person brian2 = new Person("Brian", "Goetz", 39);
            db.store(brian2);
            db.commit();
            // Buscamos todos los Brians
            ObjectSet brians = db.queryByExample(new Person("Brian", null, 0));
            while (brians.hasNext())
                System.out.println(brians.next());
        }
        finally {
            if (db != null)
                db.close();
        }
    }

    public static void PreguntaAntesDeInsertar(){
        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "persons.data");
        try {
            Scanner scr = new Scanner(System.in);
            db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "persons.data");

            //Preguntamos los datos que queremos insertar
            System.out.println("Dime el nombre de la Persona : ");
            String name = scr.next();
            System.out.println("Dime el apellido de la Persona : ");
            String lastName = scr.next();
            System.out.println("Dime la edat de la Persona : ");
            int edad = scr.nextInt();


            ObjectSet objeto = db.queryByExample(new Person(name, lastName, edad));
            //Existe la persona introducida? si no existe
            if(objeto.hasNext() == false){
                // Metemos el objeto en la base de datos
                db.store(new Person(name, lastName, edad));
                db.commit();}
            else {
                System.out.println("Esta persona ya existe en la base de datos");
            }
        }
        finally {
            if (db != null)
                db.close();
        }
    }

}
