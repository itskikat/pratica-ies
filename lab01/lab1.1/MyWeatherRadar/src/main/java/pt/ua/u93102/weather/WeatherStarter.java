package pt.ua.u93102.weather;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import pt.ua.u93102.weather.ipma_client.IpmaCityForecast;
import pt.ua.u93102.weather.ipma_client.IpmaService;

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
        Call<IpmaCityForecast> callSync = service.getForecastForACity(CITY_ID_AVEIRO);
        if (args.length != 0){
            callSync = service.getForecastForACity(Integer.parseInt(args[0]));    
        }

        try {
            Response<IpmaCityForecast> apiResponse = callSync.execute();
            IpmaCityForecast forecast = apiResponse.body();

            if (forecast != null) {

                System.out.println("DATE: " + forecast.getData().listIterator().next().getForecastDate());
                logger.info("DATE: " + forecast.getData().listIterator().next().getForecastDate());

                System.out.println("MAX TEMP: " + forecast.getData().listIterator().next().getTMax() + " *C");
                logger.info("MAX TEMP: " + forecast.getData().listIterator().next().getTMax() + " *C");

                System.out.println("MIN TEMP: " + forecast.getData().listIterator().next().getTMin() + " *C");
                logger.info("MIN TEMP: " + forecast.getData().listIterator().next().getTMin() + " *C");

                System.out.println("PRECIPITATION PROB: " + forecast.getData().listIterator().next().getPrecipitaProb());
                logger.info("PRECIPITATION PROB: " + forecast.getData().listIterator().next().getPrecipitaProb());

                
            } else {
                logger.info( "No results!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}