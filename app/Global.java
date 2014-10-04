import models.Player;
import play.Application;
import play.GlobalSettings;
import play.libs.Yaml;

import java.util.List;
import java.util.Map;

@SuppressWarnings("UnusedDeclaration")
public class Global extends GlobalSettings {

    @SuppressWarnings("unchecked")
    @Override
    public void onStart(Application application) {

        Map<String, List<Object>> initialData = (Map<String, List<Object>>) Yaml.load("initial-data.yml");

        if (Player.findById(1) == null) {
            List<Object> superusers = initialData.get("superusers");

            for (Object superuser : superusers) {
                Map<String, String> map = (Map<String, String>) superuser;
                new Player(map.get("email"), map.get("name"), map.get("password")).save();
            }
        }

    }

}
