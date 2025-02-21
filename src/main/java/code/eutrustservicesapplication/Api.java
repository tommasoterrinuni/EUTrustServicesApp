package code.eutrustservicesapplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import org.json.*;
/**
 * Classe che prende, attraverso la chiamata all'API del sito, i dati necessari e
 * definisce i metodi per filtrare quelli selezionati dall'utente
 */
public class Api {
    private static final Vector<String> countries = new Vector<>();
    private static final Vector<String> name_countries= new Vector<>();
    private static final Vector<String> type_of_services = new Vector<>();
    private static final Vector<String> services = new Vector<>();
    private static final Vector<String> providers = new Vector<>();
    private static final Vector<String> status = new Vector<>();
    private static final Vector<Node> totalNodes = new Vector<>();

    /**
     * Metodo per avviare una connessione con l'API del sito, per importare i dati sul quale
     * poi il programma effettua un filtraggio mediante ricerca e selezione/deselezione
     *
     * @throws Exception viene lanciata se il programma non riesce a effettuare una connessione con
     * l'API e, di conseguenza, a importare i dati necessari
     */
    public static void start() throws Exception {
        countries.clear();
        name_countries.clear();
        type_of_services.clear();
        status.clear();
        providers.clear();
        services.clear();
        totalNodes.clear();
        //connessione con Api contenente tutti i servizi con le relative informazioni
        URL url = new URL("https://esignature.ec.europa.eu/efda/tl-browser/api/v1/search/tsp_list");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine,total = "";
        while ((inputLine = in.readLine()) != null) {
            total = total + inputLine;
        }
        JSONArray array = new JSONArray(total); //dati salvati in un JsonArray
        //connessione con Api contenente i nomi e le sigle di tutte le nazioni
        url = new URL("https://esignature.ec.europa.eu/efda/tl-browser/api/v1/search/countries_list_no_lotl_territory");
        connection = (HttpURLConnection) url.openConnection();
        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        total = "";
        while ((inputLine = in.readLine()) != null) {
            total = total + inputLine;
        }
        JSONArray array_countries= new JSONArray(total);  //dati salvati in un JsonArray
        //recupero informazioni nazioni con metodi degli oggetti json
        for(int i=0;i<array_countries.length();i++)
        {
            String name=array_countries.getJSONObject(i).getString("countryName");
            name_countries.add(name);
            name=array_countries.getJSONObject(i).getString("countryCode");
            countries.add(name);
        }
        //recupero informazioni servizi con metodi degli oggetti json
        for (int i = 0; i < array.length(); i++) {
            String nameProvider = array.getJSONObject(i).getString("name");
            String country = array.getJSONObject(i).getString("countryCode");
            JSONArray array1 = array.getJSONObject(i).getJSONArray("services");
            for (int j = 0; j < array1.length(); j++) {
                String nameService = array1.getJSONObject(j).getString("serviceName");
                String stat = array1.getJSONObject(j).getString("currentStatus").substring(50);
                JSONArray array2 = array1.getJSONObject(j).getJSONArray("qServiceTypes");
                for (int k = 0; k < array2.length(); k++) {
                    String type = array2.getString(k);
                    totalNodes.add(new Node(country, nameProvider, nameService, stat, type));
                    if(!type_of_services.contains(type)) type_of_services.add(type);
                    if(!providers.contains(nameProvider)) providers.add(nameProvider);
                    if(!status.contains(stat)) status.add(stat);
                    if(!services.contains(nameService)) services.add(nameService);
                }
            }
        }

    }

    /**
     * Restituisce le sigle delle nazioni
     * @return le sigle delle nazioni in una lista non modificabile
     */
    public static List<String> countries() { return Collections.unmodifiableList(countries);}
    /**
     * Restituisce i nomi delle nazioni
     * @return i nomi delle nazioni in una lista non modificabile
     */
    public static List<String> name_countries() { return Collections.unmodifiableList(name_countries);}
    /**
     * Restituisce i tipi di servizi
     * @return i tipi di servizi in una lista non modificabile
     */
    public static List<String> type_of_services() { return Collections.unmodifiableList(type_of_services);}
    /**
     * Restituisce i nomi dei provider
     * @return i nomi dei provider in una lista non modificabile
     */
    public static List<String> providers() { return Collections.unmodifiableList(providers);}
    /**
     * Restituisce gli stati dei servizi
     * @return gli stati dei servizi in una lista non modificabile
     */
    public static List<String> status() { return Collections.unmodifiableList(status);}
    /**
     * Restituisce tutte le associazioni nazione-provider-servizio-tipo di servizio-stato servizio
     * @return tutte le associazioni nazione-provider-servizio-tipo di servizio-stato servizio in una lista non modificabile
     */

    public static List<Node> totalNodes() { return Collections.unmodifiableList(totalNodes);}


}
