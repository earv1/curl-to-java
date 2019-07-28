import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test2 {

  private final static Logger logger = LoggerFactory.getLogger(Test2.class);
  private final static RestTemplate restTemplate = new RestTemplate();
  private final static ObjectMapper mapper = new ObjectMapper();

  public static boolean execute() throws Exception {



    HttpEntity<String> requestEntity = new HttpEntity<>("");
    ResponseEntity<String> responseEntity =
        restTemplate.exchange(
            "https://jsonplaceholder.typicode.com/users",
            HttpMethod.GET,
            requestEntity,
            String.class);

    final JsonNode jsonNode = mapper.readValue(responseEntity.getBody(), JsonNode.class);

    if (!(jsonNode.get(0).get("id").asInt() == 1)) {
      return false;
    }
    if (!(jsonNode.get(0).get("name").asText().equals("Leanne Graham"))) {
      return false;
    }
    if (!(jsonNode.get(0).get("username").asText().equals("Bret"))) {
      return false;
    }
    if (!(jsonNode.get(0).get("email").asText().equals("Sincere@april.biz"))) {
      return false;
    }
    if (!(jsonNode.get(0).get("address").get("street").asText().equals("Kulas Light"))) {
      return false;
    }
    if (!(jsonNode.get(0).get("address").get("suite").asText().equals("Apt. 556"))) {
      return false;
    }
    if (!(jsonNode.get(0).get("address").get("city").asText().equals("Gwenborough"))) {
      return false;
    }
    if (!(jsonNode.get(0).get("address").get("zipcode").asText().equals("92998-3874"))) {
      return false;
    }
    if (!(jsonNode.get(0).get("address").get("geo").get("lat").asText().equals("-37.3159"))) {
      return false;
    }
    if (!(jsonNode.get(0).get("address").get("geo").get("lng").asText().equals("81.1496"))) {
      return false;
    }

    if (!(jsonNode.get(0).get("phone").asText().equals("1-770-736-8031 x56442"))) {
      return false;
    }
    if (!(jsonNode.get(0).get("website").asText().equals("hildegard.org"))) {
      return false;
    }
    if (!(jsonNode.get(0).get("company").get("name").asText().equals("Romaguera-Crona"))) {
      return false;
    }
    if (!(jsonNode
        .get(0)
        .get("company")
        .get("catchPhrase")
        .asText()
        .equals("Multi-layered client-server neural-net"))) {
      return false;
    }
    if (!(jsonNode
        .get(0)
        .get("company")
        .get("bs")
        .asText()
        .equals("harness real-time e-markets"))) {
      return false;
    }

    if (!(jsonNode.get(1).get("id").asInt() == 2)) {
      return false;
    }
    if (!(jsonNode.get(1).get("name").asText().equals("Ervin Howell"))) {
      return false;
    }
    if (!(jsonNode.get(1).get("username").asText().equals("Antonette"))) {
      return false;
    }
    if (!(jsonNode.get(1).get("email").asText().equals("Shanna@melissa.tv"))) {
      return false;
    }
    if (!(jsonNode.get(1).get("address").get("street").asText().equals("Victor Plains"))) {
      return false;
    }
    if (!(jsonNode.get(1).get("address").get("suite").asText().equals("Suite 879"))) {
      return false;
    }
    if (!(jsonNode.get(1).get("address").get("city").asText().equals("Wisokyburgh"))) {
      return false;
    }
    if (!(jsonNode.get(1).get("address").get("zipcode").asText().equals("90566-7771"))) {
      return false;
    }
    if (!(jsonNode.get(1).get("address").get("geo").get("lat").asText().equals("-43.9509"))) {
      return false;
    }
    if (!(jsonNode.get(1).get("address").get("geo").get("lng").asText().equals("-34.4618"))) {
      return false;
    }

    if (!(jsonNode.get(1).get("phone").asText().equals("010-692-6593 x09125"))) {
      return false;
    }
    if (!(jsonNode.get(1).get("website").asText().equals("anastasia.net"))) {
      return false;
    }
    if (!(jsonNode.get(1).get("company").get("name").asText().equals("Deckow-Crist"))) {
      return false;
    }
    if (!(jsonNode
        .get(1)
        .get("company")
        .get("catchPhrase")
        .asText()
        .equals("Proactive didactic contingency"))) {
      return false;
    }
    if (!(jsonNode
        .get(1)
        .get("company")
        .get("bs")
        .asText()
        .equals("synergize scalable supply-chains"))) {
      return false;
    }

    if (!(jsonNode.get(2).get("id").asInt() == 3)) {
      return false;
    }
    if (!(jsonNode.get(2).get("name").asText().equals("Clementine Bauch"))) {
      return false;
    }
    if (!(jsonNode.get(2).get("username").asText().equals("Samantha"))) {
      return false;
    }
    if (!(jsonNode.get(2).get("email").asText().equals("Nathan@yesenia.net"))) {
      return false;
    }
    if (!(jsonNode.get(2).get("address").get("street").asText().equals("Douglas Extension"))) {
      return false;
    }
    if (!(jsonNode.get(2).get("address").get("suite").asText().equals("Suite 847"))) {
      return false;
    }
    if (!(jsonNode.get(2).get("address").get("city").asText().equals("McKenziehaven"))) {
      return false;
    }
    if (!(jsonNode.get(2).get("address").get("zipcode").asText().equals("59590-4157"))) {
      return false;
    }
    if (!(jsonNode.get(2).get("address").get("geo").get("lat").asText().equals("-68.6102"))) {
      return false;
    }
    if (!(jsonNode.get(2).get("address").get("geo").get("lng").asText().equals("-47.0653"))) {
      return false;
    }

    if (!(jsonNode.get(2).get("phone").asText().equals("1-463-123-4447"))) {
      return false;
    }
    if (!(jsonNode.get(2).get("website").asText().equals("ramiro.info"))) {
      return false;
    }
    if (!(jsonNode.get(2).get("company").get("name").asText().equals("Romaguera-Jacobson"))) {
      return false;
    }
    if (!(jsonNode
        .get(2)
        .get("company")
        .get("catchPhrase")
        .asText()
        .equals("Face to face bifurcated interface"))) {
      return false;
    }
    if (!(jsonNode
        .get(2)
        .get("company")
        .get("bs")
        .asText()
        .equals("e-enable strategic applications"))) {
      return false;
    }

    if (!(jsonNode.get(3).get("id").asInt() == 4)) {
      return false;
    }
    if (!(jsonNode.get(3).get("name").asText().equals("Patricia Lebsack"))) {
      return false;
    }
    if (!(jsonNode.get(3).get("username").asText().equals("Karianne"))) {
      return false;
    }
    if (!(jsonNode.get(3).get("email").asText().equals("Julianne.OConner@kory.org"))) {
      return false;
    }
    if (!(jsonNode.get(3).get("address").get("street").asText().equals("Hoeger Mall"))) {
      return false;
    }
    if (!(jsonNode.get(3).get("address").get("suite").asText().equals("Apt. 692"))) {
      return false;
    }
    if (!(jsonNode.get(3).get("address").get("city").asText().equals("South Elvis"))) {
      return false;
    }
    if (!(jsonNode.get(3).get("address").get("zipcode").asText().equals("53919-4257"))) {
      return false;
    }
    if (!(jsonNode.get(3).get("address").get("geo").get("lat").asText().equals("29.4572"))) {
      return false;
    }
    if (!(jsonNode.get(3).get("address").get("geo").get("lng").asText().equals("-164.2990"))) {
      return false;
    }

    if (!(jsonNode.get(3).get("phone").asText().equals("493-170-9623 x156"))) {
      return false;
    }
    if (!(jsonNode.get(3).get("website").asText().equals("kale.biz"))) {
      return false;
    }
    if (!(jsonNode.get(3).get("company").get("name").asText().equals("Robel-Corkery"))) {
      return false;
    }
    if (!(jsonNode
        .get(3)
        .get("company")
        .get("catchPhrase")
        .asText()
        .equals("Multi-tiered zero tolerance productivity"))) {
      return false;
    }
    if (!(jsonNode
        .get(3)
        .get("company")
        .get("bs")
        .asText()
        .equals("transition cutting-edge web services"))) {
      return false;
    }

    if (!(jsonNode.get(4).get("id").asInt() == 5)) {
      return false;
    }
    if (!(jsonNode.get(4).get("name").asText().equals("Chelsey Dietrich"))) {
      return false;
    }
    if (!(jsonNode.get(4).get("username").asText().equals("Kamren"))) {
      return false;
    }
    if (!(jsonNode.get(4).get("email").asText().equals("Lucio_Hettinger@annie.ca"))) {
      return false;
    }
    if (!(jsonNode.get(4).get("address").get("street").asText().equals("Skiles Walks"))) {
      return false;
    }
    if (!(jsonNode.get(4).get("address").get("suite").asText().equals("Suite 351"))) {
      return false;
    }
    if (!(jsonNode.get(4).get("address").get("city").asText().equals("Roscoeview"))) {
      return false;
    }
    if (!(jsonNode.get(4).get("address").get("zipcode").asText().equals("33263"))) {
      return false;
    }
    if (!(jsonNode.get(4).get("address").get("geo").get("lat").asText().equals("-31.8129"))) {
      return false;
    }
    if (!(jsonNode.get(4).get("address").get("geo").get("lng").asText().equals("62.5342"))) {
      return false;
    }

    if (!(jsonNode.get(4).get("phone").asText().equals("(254)954-1289"))) {
      return false;
    }
    if (!(jsonNode.get(4).get("website").asText().equals("demarco.info"))) {
      return false;
    }
    if (!(jsonNode.get(4).get("company").get("name").asText().equals("Keebler LLC"))) {
      return false;
    }
    if (!(jsonNode
        .get(4)
        .get("company")
        .get("catchPhrase")
        .asText()
        .equals("User-centric fault-tolerant solution"))) {
      return false;
    }
    if (!(jsonNode
        .get(4)
        .get("company")
        .get("bs")
        .asText()
        .equals("revolutionize end-to-end systems"))) {
      return false;
    }

    if (!(jsonNode.get(5).get("id").asInt() == 6)) {
      return false;
    }
    if (!(jsonNode.get(5).get("name").asText().equals("Mrs. Dennis Schulist"))) {
      return false;
    }
    if (!(jsonNode.get(5).get("username").asText().equals("Leopoldo_Corkery"))) {
      return false;
    }
    if (!(jsonNode.get(5).get("email").asText().equals("Karley_Dach@jasper.info"))) {
      return false;
    }
    if (!(jsonNode.get(5).get("address").get("street").asText().equals("Norberto Crossing"))) {
      return false;
    }
    if (!(jsonNode.get(5).get("address").get("suite").asText().equals("Apt. 950"))) {
      return false;
    }
    if (!(jsonNode.get(5).get("address").get("city").asText().equals("South Christy"))) {
      return false;
    }
    if (!(jsonNode.get(5).get("address").get("zipcode").asText().equals("23505-1337"))) {
      return false;
    }
    if (!(jsonNode.get(5).get("address").get("geo").get("lat").asText().equals("-71.4197"))) {
      return false;
    }
    if (!(jsonNode.get(5).get("address").get("geo").get("lng").asText().equals("71.7478"))) {
      return false;
    }

    if (!(jsonNode.get(5).get("phone").asText().equals("1-477-935-8478 x6430"))) {
      return false;
    }
    if (!(jsonNode.get(5).get("website").asText().equals("ola.org"))) {
      return false;
    }
    if (!(jsonNode.get(5).get("company").get("name").asText().equals("Considine-Lockman"))) {
      return false;
    }
    if (!(jsonNode
        .get(5)
        .get("company")
        .get("catchPhrase")
        .asText()
        .equals("Synchronised bottom-line interface"))) {
      return false;
    }
    if (!(jsonNode
        .get(5)
        .get("company")
        .get("bs")
        .asText()
        .equals("e-enable innovative applications"))) {
      return false;
    }

    if (!(jsonNode.get(6).get("id").asInt() == 7)) {
      return false;
    }
    if (!(jsonNode.get(6).get("name").asText().equals("Kurtis Weissnat"))) {
      return false;
    }
    if (!(jsonNode.get(6).get("username").asText().equals("Elwyn.Skiles"))) {
      return false;
    }
    if (!(jsonNode.get(6).get("email").asText().equals("Telly.Hoeger@billy.biz"))) {
      return false;
    }
    if (!(jsonNode.get(6).get("address").get("street").asText().equals("Rex Trail"))) {
      return false;
    }
    if (!(jsonNode.get(6).get("address").get("suite").asText().equals("Suite 280"))) {
      return false;
    }
    if (!(jsonNode.get(6).get("address").get("city").asText().equals("Howemouth"))) {
      return false;
    }
    if (!(jsonNode.get(6).get("address").get("zipcode").asText().equals("58804-1099"))) {
      return false;
    }
    if (!(jsonNode.get(6).get("address").get("geo").get("lat").asText().equals("24.8918"))) {
      return false;
    }
    if (!(jsonNode.get(6).get("address").get("geo").get("lng").asText().equals("21.8984"))) {
      return false;
    }

    if (!(jsonNode.get(6).get("phone").asText().equals("210.067.6132"))) {
      return false;
    }
    if (!(jsonNode.get(6).get("website").asText().equals("elvis.io"))) {
      return false;
    }
    if (!(jsonNode.get(6).get("company").get("name").asText().equals("Johns Group"))) {
      return false;
    }
    if (!(jsonNode
        .get(6)
        .get("company")
        .get("catchPhrase")
        .asText()
        .equals("Configurable multimedia task-force"))) {
      return false;
    }
    if (!(jsonNode
        .get(6)
        .get("company")
        .get("bs")
        .asText()
        .equals("generate enterprise e-tailers"))) {
      return false;
    }

    if (!(jsonNode.get(7).get("id").asInt() == 8)) {
      return false;
    }
    if (!(jsonNode.get(7).get("name").asText().equals("Nicholas Runolfsdottir V"))) {
      return false;
    }
    if (!(jsonNode.get(7).get("username").asText().equals("Maxime_Nienow"))) {
      return false;
    }
    if (!(jsonNode.get(7).get("email").asText().equals("Sherwood@rosamond.me"))) {
      return false;
    }
    if (!(jsonNode.get(7).get("address").get("street").asText().equals("Ellsworth Summit"))) {
      return false;
    }
    if (!(jsonNode.get(7).get("address").get("suite").asText().equals("Suite 729"))) {
      return false;
    }
    if (!(jsonNode.get(7).get("address").get("city").asText().equals("Aliyaview"))) {
      return false;
    }
    if (!(jsonNode.get(7).get("address").get("zipcode").asText().equals("45169"))) {
      return false;
    }
    if (!(jsonNode.get(7).get("address").get("geo").get("lat").asText().equals("-14.3990"))) {
      return false;
    }
    if (!(jsonNode.get(7).get("address").get("geo").get("lng").asText().equals("-120.7677"))) {
      return false;
    }

    if (!(jsonNode.get(7).get("phone").asText().equals("586.493.6943 x140"))) {
      return false;
    }
    if (!(jsonNode.get(7).get("website").asText().equals("jacynthe.com"))) {
      return false;
    }
    if (!(jsonNode.get(7).get("company").get("name").asText().equals("Abernathy Group"))) {
      return false;
    }
    if (!(jsonNode
        .get(7)
        .get("company")
        .get("catchPhrase")
        .asText()
        .equals("Implemented secondary concept"))) {
      return false;
    }
    if (!(jsonNode
        .get(7)
        .get("company")
        .get("bs")
        .asText()
        .equals("e-enable extensible e-tailers"))) {
      return false;
    }

    if (!(jsonNode.get(8).get("id").asInt() == 9)) {
      return false;
    }
    if (!(jsonNode.get(8).get("name").asText().equals("Glenna Reichert"))) {
      return false;
    }
    if (!(jsonNode.get(8).get("username").asText().equals("Delphine"))) {
      return false;
    }
    if (!(jsonNode.get(8).get("email").asText().equals("Chaim_McDermott@dana.io"))) {
      return false;
    }
    if (!(jsonNode.get(8).get("address").get("street").asText().equals("Dayna Park"))) {
      return false;
    }
    if (!(jsonNode.get(8).get("address").get("suite").asText().equals("Suite 449"))) {
      return false;
    }
    if (!(jsonNode.get(8).get("address").get("city").asText().equals("Bartholomebury"))) {
      return false;
    }
    if (!(jsonNode.get(8).get("address").get("zipcode").asText().equals("76495-3109"))) {
      return false;
    }
    if (!(jsonNode.get(8).get("address").get("geo").get("lat").asText().equals("24.6463"))) {
      return false;
    }
    if (!(jsonNode.get(8).get("address").get("geo").get("lng").asText().equals("-168.8889"))) {
      return false;
    }

    if (!(jsonNode.get(8).get("phone").asText().equals("(775)976-6794 x41206"))) {
      return false;
    }
    if (!(jsonNode.get(8).get("website").asText().equals("conrad.com"))) {
      return false;
    }
    if (!(jsonNode.get(8).get("company").get("name").asText().equals("Yost and Sons"))) {
      return false;
    }
    if (!(jsonNode
        .get(8)
        .get("company")
        .get("catchPhrase")
        .asText()
        .equals("Switchable contextually-based project"))) {
      return false;
    }
    if (!(jsonNode
        .get(8)
        .get("company")
        .get("bs")
        .asText()
        .equals("aggregate real-time technologies"))) {
      return false;
    }

    if (!(jsonNode.get(9).get("id").asInt() == 10)) {
      return false;
    }
    if (!(jsonNode.get(9).get("name").asText().equals("Clementina DuBuque"))) {
      return false;
    }
    if (!(jsonNode.get(9).get("username").asText().equals("Moriah.Stanton"))) {
      return false;
    }
    if (!(jsonNode.get(9).get("email").asText().equals("Rey.Padberg@karina.biz"))) {
      return false;
    }
    if (!(jsonNode.get(9).get("address").get("street").asText().equals("Kattie Turnpike"))) {
      return false;
    }
    if (!(jsonNode.get(9).get("address").get("suite").asText().equals("Suite 198"))) {
      return false;
    }
    if (!(jsonNode.get(9).get("address").get("city").asText().equals("Lebsackbury"))) {
      return false;
    }
    if (!(jsonNode.get(9).get("address").get("zipcode").asText().equals("31428-2261"))) {
      return false;
    }
    if (!(jsonNode.get(9).get("address").get("geo").get("lat").asText().equals("-38.2386"))) {
      return false;
    }
    if (!(jsonNode.get(9).get("address").get("geo").get("lng").asText().equals("57.2232"))) {
      return false;
    }

    if (!(jsonNode.get(9).get("phone").asText().equals("024-648-3804"))) {
      return false;
    }
    if (!(jsonNode.get(9).get("website").asText().equals("ambrose.net"))) {
      return false;
    }
    if (!(jsonNode.get(9).get("company").get("name").asText().equals("Hoeger LLC"))) {
      return false;
    }
    if (!(jsonNode
        .get(9)
        .get("company")
        .get("catchPhrase")
        .asText()
        .equals("Centralized empowering task-force"))) {
      return false;
    }
    if (!(jsonNode.get(9).get("company").get("bs").asText().equals("target end-to-end models"))) {
      return false;
    }

    boolean success = true;
    return success;
  }
}

