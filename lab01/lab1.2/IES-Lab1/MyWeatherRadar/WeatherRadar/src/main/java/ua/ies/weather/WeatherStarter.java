package ua.ies.weather;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.ies.weather.ipma.IpmaCityForecast;
import ua.ies.weather.ipma.IpmaService;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * demonstrates the use of the IPMA API for weather forecast
 */
public class WeatherStarter {

    private static final int CITY_ID_AVEIRO = 1010500;
    /*
    loggers provide a better alternative to System.out.println
    https://rules.sonarsource.com/java/tag/bad-practice/RSPEC-106
     */
    private static final Logger logger = Logger.getLogger(WeatherStarter.class.getName());

    public static void  main(String[] args ) {

        /*
        get a retrofit instance, loaded with the GSon lib to convert JSON into objects
         */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.ipma.pt/open-data/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IpmaService service = retrofit.create(IpmaService.class);
        HashMap<String,Integer> cities_code =initialize_cities();
        Call<IpmaCityForecast> callSync = service.getForecastForACity(cities_code.get( args[0]));

        try {
            Response<IpmaCityForecast> apiResponse = callSync.execute();
            IpmaCityForecast forecast = apiResponse.body();

            if (forecast != null) {
                logger.info(  "\n  Temperature maximum: " + forecast.getData().
                        listIterator().next().getTMax()+ "C");
                System.out.println("Temperature minimum: " + forecast.getData().
                listIterator().next().getTMin()+ "C");
                System.out.println("Probability of precipitation: " + forecast.getData().
                listIterator().next().getPrecipitaProb() + "%");
                System.out.println("Latitude: "+forecast.getData().
                listIterator().next().getLatitude() +" Longitude: " + forecast.getData().
                listIterator().next().getLongitude() );
            } else {
                logger.info( "No results!");
            }
            System.exit(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    public static HashMap<String,Integer> initialize_cities(){
        HashMap<String,Integer> cities_code = new HashMap();
        cities_code.put("Aveiro",1010500);
        cities_code.put("Beja",1020500);
        cities_code.put("Braga",1030300);
        cities_code.put("Bragança",1040200);
        cities_code.put("Castelo Branco",1050200);
        cities_code.put("Coimbra",1060300);
        cities_code.put("Évora",1070500);
        cities_code.put("Faro",1080500);
        cities_code.put("Guarda",1090700);
        cities_code.put("Leiria",1100900);
        cities_code.put("Lisboa",1110600);
        cities_code.put("Portalegre",1121400);
        cities_code.put("Porto",1131200);
        cities_code.put("Santarém",1141600);
        cities_code.put("Viana do Castelo",1160900);
        cities_code.put("Vila Real",1171400);
        cities_code.put("Viseu",1182300);
        cities_code.put("Funchal",2310300);
        cities_code.put("Porto Santo",2320100);
        cities_code.put("Vila do Porto",3410100);
        cities_code.put("Ponta Delgada",3420300);
        cities_code.put("Angra do Heroísmo",3430100);
        cities_code.put("Santa Cruz da Graciosa",3440100);
        cities_code.put("Velas",3450200);
        cities_code.put("Madalena",3460200);
        cities_code.put("Horta",3470100);
        cities_code.put("Santa Cruz das Flores",3480200);
        cities_code.put("Vila do Corvo",3490100);
        return cities_code;
    }

}