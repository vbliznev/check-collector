package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)

public class PojoJson {
    public static class Data {
        public Json json;
        public String html;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Gs1m {
        public String gtin;
        public String sernum;
        public int productIdType;
        public String rawProductCode;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        public int nds;
        public int sum;
        public String name;
        public float price;
        public float quantity;
        public int paymentType;
        public int productType;
        public ProductCodeNew productCodeNew;
        public int labelCodeProcesMode;
        public ArrayList<ItemsIndustryDetail> itemsIndustryDetails;
        public int itemsQuantityMeasure;
        public int checkingProdInformationResult;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ItemsIndustryDetail {
        public String idFoiv;
        public String industryPropValue;
        public String foundationDocNumber;
        public String foundationDocDateTime;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Json {
        public int code;
        public String user;
        public ArrayList<Item> items;
        public int nds18;
        public String region;
        public String userInn;
        public Date dateTime;
        public String kktRegId;
        public Metadata metadata;
        public String operator;
        public int totalSum;
        public int creditSum;
        public String numberKkt;
        public long fiscalSign;
        public int prepaidSum;
        public String retailPlace;
        public int shiftNumber;
        public int cashTotalSum;
        public int provisionSum;
        public int ecashTotalSum;
        public int operationType;
        public int redefine_mask;
        public int requestNumber;
        public String fiscalDriveNumber;
        public double messageFiscalSign;
        public String retailPlaceAddress;
        public int appliedTaxationType;
        public int fiscalDocumentNumber;
        public int fiscalDocumentFormatVer;
        public int checkingLabeledProdResult;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Manual {
        public String fn;
        public String fd;
        public String fp;
        public String check_time;
        public String type;
        public String sum;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Metadata {
        public long id;
        public String ofdId;
        public String address;
        public String subtype;
        public Date receiveDate;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductCodeNew {
        public Gs1m gs1m;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Request {
        public String qrurl;
        public String qrfile;
        public String qrraw;
        public Manual manual;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Root {
        public int code;
        public int first;
        public Data data;
        public Request request;
    }


}
//       String json1 = "{\n" +
//                "  \"code\": 1,\n" +
//                "  \"first\": 0,\n" +
//                "  \"data\": {\n" +
//                "    \"json\": {\n" +
//                "      \"code\": 3,\n" +
//                "      \"user\": \"ООО \\\"О'КЕЙ\\\"\",\n" +
//                "      \"items\": [\n" +
//                "        {\n" +
//                "          \"nds\": 1,\n" +
//                "          \"sum\": 2819,\n" +
//                "          \"name\": \"Вода Сенежская питьев.негаз.0,5л ПЭТ\",\n" +
//                "          \"price\": 2819,\n" +
//                "          \"ndsSum\": 470,\n" +
//                "          \"quantity\": 1,\n" +
//                "          \"paymentType\": 4,\n" +
//                "          \"productType\": 33,\n" +
//                "          \"productCodeNew\": {\n" +
//                "            \"gs1m\": {\n" +
//                "              \"gtin\": \"04600286001911\",\n" +
//                "              \"sernum\": \"5.DvEQD>vTTto\",\n" +
//                "              \"productIdType\": 6,\n" +
//                "              \"rawProductCode\": \"0104600286001911215.DvEQD>vTTto\"\n" +
//                "            }\n" +
//                "          },\n" +
//                "          \"labelCodeProcesMode\": 0,\n" +
//                "          \"itemsIndustryDetails\": [\n" +
//                "            {\n" +
//                "              \"idFoiv\": \"030\",\n" +
//                "              \"industryPropValue\": \"UUID=9d1cb808-fd0f-443d-b8fc-ed92dd0a8d33&Time=1737830919427\",\n" +
//                "              \"foundationDocNumber\": \"1944\",\n" +
//                "              \"foundationDocDateTime\": \"21.11.2023\"\n" +
//                "            }\n" +
//                "          ],\n" +
//                "          \"itemsQuantityMeasure\": 0,\n" +
//                "          \"checkingProdInformationResult\": 15\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"nds\": 1,\n" +
//                "          \"sum\": 8399,\n" +
//                "          \"name\": \"Чипсы картофельные Mega Chips со вкусом сметаны и лука 100г\",\n" +
//                "          \"price\": 8399,\n" +
//                "          \"ndsSum\": 1400,\n" +
//                "          \"quantity\": 1,\n" +
//                "          \"paymentType\": 4,\n" +
//                "          \"productType\": 1,\n" +
//                "          \"itemsQuantityMeasure\": 0\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"nds\": 1,\n" +
//                "          \"sum\": 8499,\n" +
//                "          \"name\": \"Пивной напиток Крон Бланш Бьер нефильтованный пастеризованный 4,5% 0,45л ж\\/б Балтика\",\n" +
//                "          \"price\": 8499,\n" +
//                "          \"ndsSum\": 1417,\n" +
//                "          \"quantity\": 1,\n" +
//                "          \"paymentType\": 4,\n" +
//                "          \"productType\": 31,\n" +
//                "          \"productCodeNew\": {\n" +
//                "            \"gs1m\": {\n" +
//                "              \"gtin\": \"04600682041986\",\n" +
//                "              \"sernum\": \"5-tO7.h\",\n" +
//                "              \"productIdType\": 6,\n" +
//                "              \"rawProductCode\": \"0104600682041986215-tO7.h\"\n" +
//                "            }\n" +
//                "          },\n" +
//                "          \"labelCodeProcesMode\": 0,\n" +
//                "          \"itemsIndustryDetails\": [\n" +
//                "            {\n" +
//                "              \"idFoiv\": \"030\",\n" +
//                "              \"industryPropValue\": \"UUID=349c82f9-39f9-46f9-aeae-eea503f15a44&Time=1737830934073\",\n" +
//                "              \"foundationDocNumber\": \"1944\",\n" +
//                "              \"foundationDocDateTime\": \"21.11.2023\"\n" +
//                "            }\n" +
//                "          ],\n" +
//                "          \"itemsQuantityMeasure\": 0,\n" +
//                "          \"checkingProdInformationResult\": 15\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"nds\": 1,\n" +
//                "          \"sum\": 8499,\n" +
//                "          \"name\": \"Пивной напиток Крон Бланш Бьер нефильтованный пастеризованный 4,5% 0,45л ж\\/б Балтика\",\n" +
//                "          \"price\": 8499,\n" +
//                "          \"ndsSum\": 1417,\n" +
//                "          \"quantity\": 1,\n" +
//                "          \"paymentType\": 4,\n" +
//                "          \"productType\": 31,\n" +
//                "          \"productCodeNew\": {\n" +
//                "            \"gs1m\": {\n" +
//                "              \"gtin\": \"04600682041986\",\n" +
//                "              \"sernum\": \"5lKTOIv\",\n" +
//                "              \"productIdType\": 6,\n" +
//                "              \"rawProductCode\": \"0104600682041986215lKTOIv\"\n" +
//                "            }\n" +
//                "          },\n" +
//                "          \"labelCodeProcesMode\": 0,\n" +
//                "          \"itemsIndustryDetails\": [\n" +
//                "            {\n" +
//                "              \"idFoiv\": \"030\",\n" +
//                "              \"industryPropValue\": \"UUID=ebe1999c-be9f-4f12-9cb5-6f30f31790ba&Time=1737830947811\",\n" +
//                "              \"foundationDocNumber\": \"1944\",\n" +
//                "              \"foundationDocDateTime\": \"21.11.2023\"\n" +
//                "            }\n" +
//                "          ],\n" +
//                "          \"itemsQuantityMeasure\": 0,\n" +
//                "          \"checkingProdInformationResult\": 15\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"nds\": 1,\n" +
//                "          \"sum\": 8499,\n" +
//                "          \"name\": \"Пивной напиток Крон Бланш Бьер нефильтованный пастеризованный 4,5% 0,45л ж\\/б Балтика\",\n" +
//                "          \"price\": 8499,\n" +
//                "          \"ndsSum\": 1417,\n" +
//                "          \"quantity\": 1,\n" +
//                "          \"paymentType\": 4,\n" +
//                "          \"productType\": 31,\n" +
//                "          \"productCodeNew\": {\n" +
//                "            \"gs1m\": {\n" +
//                "              \"gtin\": \"04600682041986\",\n" +
//                "              \"sernum\": \"5DMkYIe\",\n" +
//                "              \"productIdType\": 6,\n" +
//                "              \"rawProductCode\": \"0104600682041986215DMkYIe\"\n" +
//                "            }\n" +
//                "          },\n" +
//                "          \"labelCodeProcesMode\": 0,\n" +
//                "          \"itemsIndustryDetails\": [\n" +
//                "            {\n" +
//                "              \"idFoiv\": \"030\",\n" +
//                "              \"industryPropValue\": \"UUID=b38ba1b0-2dd1-474a-8a68-ef5490c7d699&Time=1737830954072\",\n" +
//                "              \"foundationDocNumber\": \"1944\",\n" +
//                "              \"foundationDocDateTime\": \"21.11.2023\"\n" +
//                "            }\n" +
//                "          ],\n" +
//                "          \"itemsQuantityMeasure\": 0,\n" +
//                "          \"checkingProdInformationResult\": 15\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"nds\": 1,\n" +
//                "          \"sum\": 29999,\n" +
//                "          \"name\": \"Вино Ай-Петри Алиготе белое полусладкое 11% 0,75л (Россия, Крым) \\/ ВИНО РОССИИ\",\n" +
//                "          \"price\": 29999,\n" +
//                "          \"ndsSum\": 5000,\n" +
//                "          \"quantity\": 1,\n" +
//                "          \"paymentType\": 4,\n" +
//                "          \"productType\": 2,\n" +
//                "          \"itemsQuantityMeasure\": 0\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"nds\": 1,\n" +
//                "          \"sum\": 8599,\n" +
//                "          \"name\": \"Пиво Бад светлое пастериз фильтр 5,0% 0,45л ж\\/б АБ ИнБев Эфес\",\n" +
//                "          \"price\": 8599,\n" +
//                "          \"ndsSum\": 1433,\n" +
//                "          \"quantity\": 1,\n" +
//                "          \"paymentType\": 4,\n" +
//                "          \"productType\": 31,\n" +
//                "          \"productCodeNew\": {\n" +
//                "            \"gs1m\": {\n" +
//                "              \"gtin\": \"04600721015398\",\n" +
//                "              \"sernum\": \"5pThM+w\",\n" +
//                "              \"productIdType\": 6,\n" +
//                "              \"rawProductCode\": \"0104600721015398215pThM+w\"\n" +
//                "            }\n" +
//                "          },\n" +
//                "          \"labelCodeProcesMode\": 0,\n" +
//                "          \"itemsIndustryDetails\": [\n" +
//                "            {\n" +
//                "              \"idFoiv\": \"030\",\n" +
//                "              \"industryPropValue\": \"UUID=f3b5a5f5-f20f-4ef5-bb18-40d1d04e1ddb&Time=1737831065402\",\n" +
//                "              \"foundationDocNumber\": \"1944\",\n" +
//                "              \"foundationDocDateTime\": \"21.11.2023\"\n" +
//                "            }\n" +
//                "          ],\n" +
//                "          \"itemsQuantityMeasure\": 0,\n" +
//                "          \"checkingProdInformationResult\": 15\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"nds\": 2,\n" +
//                "          \"sum\": 5499,\n" +
//                "          \"name\": \"Сухарики ХРУСTEAM Барные сырные палочки 70г\",\n" +
//                "          \"price\": 5499,\n" +
//                "          \"ndsSum\": 500,\n" +
//                "          \"quantity\": 1,\n" +
//                "          \"paymentType\": 4,\n" +
//                "          \"productType\": 1,\n" +
//                "          \"itemsQuantityMeasure\": 0\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"nds\": 1,\n" +
//                "          \"sum\": 7999,\n" +
//                "          \"name\": \"Пивной напиток Эсса со вкусом и ароматом апельсина и вишни пастеризованный 6,5% 0,4л ст\\/б АБ ИнБев Эфес\",\n" +
//                "          \"price\": 7999,\n" +
//                "          \"ndsSum\": 1333,\n" +
//                "          \"quantity\": 1,\n" +
//                "          \"paymentType\": 4,\n" +
//                "          \"productType\": 31,\n" +
//                "          \"productCodeNew\": {\n" +
//                "            \"gs1m\": {\n" +
//                "              \"gtin\": \"04600721026691\",\n" +
//                "              \"sernum\": \"5?Ff*5E\",\n" +
//                "              \"productIdType\": 6,\n" +
//                "              \"rawProductCode\": \"0104600721026691215?Ff*5E\"\n" +
//                "            }\n" +
//                "          },\n" +
//                "          \"labelCodeProcesMode\": 0,\n" +
//                "          \"itemsIndustryDetails\": [\n" +
//                "            {\n" +
//                "              \"idFoiv\": \"030\",\n" +
//                "              \"industryPropValue\": \"UUID=a67fa22c-9f00-48a7-9374-4392ebaf901a&Time=1737831077628\",\n" +
//                "              \"foundationDocNumber\": \"1944\",\n" +
//                "              \"foundationDocDateTime\": \"21.11.2023\"\n" +
//                "            }\n" +
//                "          ],\n" +
//                "          \"itemsQuantityMeasure\": 0,\n" +
//                "          \"checkingProdInformationResult\": 15\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"nds\": 1,\n" +
//                "          \"sum\": 8599,\n" +
//                "          \"name\": \"Пиво Бад светлое пастериз фильтр 5,0% 0,45л ж\\/б АБ ИнБев Эфес\",\n" +
//                "          \"price\": 8599,\n" +
//                "          \"ndsSum\": 1433,\n" +
//                "          \"quantity\": 1,\n" +
//                "          \"paymentType\": 4,\n" +
//                "          \"productType\": 31,\n" +
//                "          \"productCodeNew\": {\n" +
//                "            \"gs1m\": {\n" +
//                "              \"gtin\": \"04600721015398\",\n" +
//                "              \"sernum\": \"55\\\"F0iP\",\n" +
//                "              \"productIdType\": 6,\n" +
//                "              \"rawProductCode\": \"01046007210153982155\\\"F0iP\"\n" +
//                "            }\n" +
//                "          },\n" +
//                "          \"labelCodeProcesMode\": 0,\n" +
//                "          \"itemsIndustryDetails\": [\n" +
//                "            {\n" +
//                "              \"idFoiv\": \"030\",\n" +
//                "              \"industryPropValue\": \"UUID=7784c710-531a-44fb-ae86-39c9a2ce5957&Time=1737831083108\",\n" +
//                "              \"foundationDocNumber\": \"1944\",\n" +
//                "              \"foundationDocDateTime\": \"21.11.2023\"\n" +
//                "            }\n" +
//                "          ],\n" +
//                "          \"itemsQuantityMeasure\": 0,\n" +
//                "          \"checkingProdInformationResult\": 15\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"nds\": 2,\n" +
//                "          \"sum\": 17499,\n" +
//                "          \"name\": \"БЗМЖ Сыр Камамбер OKEY Selection 50% 125г\",\n" +
//                "          \"price\": 17499,\n" +
//                "          \"ndsSum\": 1591,\n" +
//                "          \"quantity\": 1,\n" +
//                "          \"paymentType\": 4,\n" +
//                "          \"productType\": 33,\n" +
//                "          \"productCodeNew\": {\n" +
//                "            \"gs1m\": {\n" +
//                "              \"gtin\": \"04650100421142\",\n" +
//                "              \"sernum\": \"5my?,U\",\n" +
//                "              \"productIdType\": 6,\n" +
//                "              \"rawProductCode\": \"0104650100421142215my?,U\"\n" +
//                "            }\n" +
//                "          },\n" +
//                "          \"labelCodeProcesMode\": 0,\n" +
//                "          \"itemsIndustryDetails\": [\n" +
//                "            {\n" +
//                "              \"idFoiv\": \"030\",\n" +
//                "              \"industryPropValue\": \"UUID=9b9b433a-83e2-46d9-9f8f-d33f4f553f9d&Time=1737831089606\",\n" +
//                "              \"foundationDocNumber\": \"1944\",\n" +
//                "              \"foundationDocDateTime\": \"21.11.2023\"\n" +
//                "            }\n" +
//                "          ],\n" +
//                "          \"itemsQuantityMeasure\": 0,\n" +
//                "          \"checkingProdInformationResult\": 15\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"nds\": 1,\n" +
//                "          \"sum\": 38999,\n" +
//                "          \"name\": \"Карбонад в\\/к в\\/у 350г Велком\",\n" +
//                "          \"price\": 38999,\n" +
//                "          \"ndsSum\": 6500,\n" +
//                "          \"quantity\": 1,\n" +
//                "          \"paymentType\": 4,\n" +
//                "          \"productType\": 1,\n" +
//                "          \"itemsQuantityMeasure\": 0\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"nds\": 2,\n" +
//                "          \"sum\": 4799,\n" +
//                "          \"name\": \"Гренки ржаные BEERka Бородинские Чеснок\\/Укроп60г+ соус25г\",\n" +
//                "          \"price\": 4799,\n" +
//                "          \"ndsSum\": 436,\n" +
//                "          \"quantity\": 1,\n" +
//                "          \"paymentType\": 4,\n" +
//                "          \"productType\": 1,\n" +
//                "          \"itemsQuantityMeasure\": 0\n" +
//                "        }\n" +
//                "      ],\n" +
//                "      \"nds10\": 2527,\n" +
//                "      \"nds18\": 21820,\n" +
//                "      \"fnsUrl\": \"www.nalog.gov.ru\",\n" +
//                "      \"region\": \"77\",\n" +
//                "      \"userInn\": \"7826087713  \",\n" +
//                "      \"dateTime\": \"2025-01-25T21:52:00\",\n" +
//                "      \"kktRegId\": \"0000501830040905    \",\n" +
//                "      \"metadata\": {\n" +
//                "        \"id\": 5667866985989607936,\n" +
//                "        \"ofdId\": \"ofd7\",\n" +
//                "        \"address\": \"125212,Россия,город федерального значения Москва,муниципальный округ Головинский вн.тер.г.,,,,Головинское ш,,д. 5,к. 1,,помещение 1,\",\n" +
//                "        \"subtype\": \"receipt\",\n" +
//                "        \"receiveDate\": \"2025-01-25T18:53:18Z\"\n" +
//                "      },\n" +
//                "      \"totalSum\": 158707,\n" +
//                "      \"creditSum\": 0,\n" +
//                "      \"numberKkt\": \"0128038706\",\n" +
//                "      \"fiscalSign\": 2168372554,\n" +
//                "      \"prepaidSum\": 0,\n" +
//                "      \"retailPlace\": \"ГМ \\\"Головинское, Водный\\\"№266\",\n" +
//                "      \"shiftNumber\": 380,\n" +
//                "      \"cashTotalSum\": 0,\n" +
//                "      \"provisionSum\": 0,\n" +
//                "      \"ecashTotalSum\": 158707,\n" +
//                "      \"machineNumber\": \"13-53964417\",\n" +
//                "      \"operationType\": 1,\n" +
//                "      \"redefine_mask\": 0,\n" +
//                "      \"requestNumber\": 168,\n" +
//                "      \"sellerAddress\": \"noreply@ofd.ru\",\n" +
//                "      \"fiscalDriveNumber\": \"7281440701464912\",\n" +
//                "      \"messageFiscalSign\": 9.297134320727882e+18,\n" +
//                "      \"retailPlaceAddress\": \"125212, г. Москва Головинское ш., д.5, корп.1, помещение 1\",\n" +
//                "      \"appliedTaxationType\": 1,\n" +
//                "      \"buyerPhoneOrAddress\": \"none\",\n" +
//                "      \"fiscalDocumentNumber\": 81838,\n" +
//                "      \"fiscalDocumentFormatVer\": 4,\n" +
//                "      \"checkingLabeledProdResult\": 0\n" +
//                "    },\n" +
//                "    \"html\": \"<table class=\\\"b-check_table table\\\"><tbody><tr class=\\\"b-check_vblock-middle b-check_center\\\"><td colspan=\\\"5\\\">ООО \\\"О'КЕЙ\\\"<\\/td><\\/tr><tr class=\\\"b-check_vblock-middle b-check_center\\\"><td colspan=\\\"5\\\">125212, г. Москва Головинское ш., д.5, корп.1, помещение 1<\\/td><\\/tr><tr class=\\\"b-check_vblock-middle b-check_center\\\"><td colspan=\\\"5\\\">ИНН 7826087713  <\\/td><\\/tr><tr class=\\\"b-check_vblock-middle b-check_center\\\"><td colspan=\\\"5\\\">&nbsp;<\\/td><\\/tr><tr class=\\\"b-check_vblock-middle b-check_center\\\"><td colspan=\\\"5\\\">25.01.2025 21:52<\\/td><\\/tr><tr class=\\\"b-check_vblock-middle b-check_center\\\"><td colspan=\\\"5\\\">Чек № 168<\\/td><\\/tr><tr class=\\\"b-check_vblock-middle b-check_center\\\"><td colspan=\\\"5\\\">Смена № 380<\\/td><\\/tr><tr class=\\\"b-check_vblock-middle b-check_center\\\"><td colspan=\\\"5\\\">Кассир <\\/td><\\/tr><tr class=\\\"b-check_vblock-last b-check_center\\\"><td colspan=\\\"5\\\">Приход<\\/td><\\/tr><tr><td><strong>№<\\/strong><\\/td><td><strong>Название<\\/strong><\\/td><td><strong>Цена<\\/strong><\\/td><td><strong>Кол.<\\/strong><\\/td><td><strong>Сумма<\\/strong><\\/td><\\/tr><tr class=\\\"b-check_item\\\"><td>1<\\/td><td>Вода Сенежская питьев.негаз.0,5л ПЭТ<\\/td><td>28.19<\\/td><td>1<\\/td><td>28.19<\\/td><\\/tr><tr class=\\\"b-check_item\\\"><td>2<\\/td><td>Чипсы картофельные Mega Chips со вкусом сметаны и лука 100г<\\/td><td>83.99<\\/td><td>1<\\/td><td>83.99<\\/td><\\/tr><tr class=\\\"b-check_item\\\"><td>3<\\/td><td>Пивной напиток Крон Бланш Бьер нефильтованный пастеризованный 4,5% 0,45л ж\\/б Балтика<\\/td><td>84.99<\\/td><td>1<\\/td><td>84.99<\\/td><\\/tr><tr class=\\\"b-check_item\\\"><td>4<\\/td><td>Пивной напиток Крон Бланш Бьер нефильтованный пастеризованный 4,5% 0,45л ж\\/б Балтика<\\/td><td>84.99<\\/td><td>1<\\/td><td>84.99<\\/td><\\/tr><tr class=\\\"b-check_item\\\"><td>5<\\/td><td>Пивной напиток Крон Бланш Бьер нефильтованный пастеризованный 4,5% 0,45л ж\\/б Балтика<\\/td><td>84.99<\\/td><td>1<\\/td><td>84.99<\\/td><\\/tr><tr class=\\\"b-check_item\\\"><td>6<\\/td><td>Вино Ай-Петри Алиготе белое полусладкое 11% 0,75л (Россия, Крым) \\/ ВИНО РОССИИ<\\/td><td>299.99<\\/td><td>1<\\/td><td>299.99<\\/td><\\/tr><tr class=\\\"b-check_item\\\"><td>7<\\/td><td>Пиво Бад светлое пастериз фильтр 5,0% 0,45л ж\\/б АБ ИнБев Эфес<\\/td><td>85.99<\\/td><td>1<\\/td><td>85.99<\\/td><\\/tr><tr class=\\\"b-check_item\\\"><td>8<\\/td><td>Сухарики ХРУСTEAM Барные сырные палочки 70г<\\/td><td>54.99<\\/td><td>1<\\/td><td>54.99<\\/td><\\/tr><tr class=\\\"b-check_item\\\"><td>9<\\/td><td>Пивной напиток Эсса со вкусом и ароматом апельсина и вишни пастеризованный 6,5% 0,4л ст\\/б АБ ИнБев Эфес<\\/td><td>79.99<\\/td><td>1<\\/td><td>79.99<\\/td><\\/tr><tr class=\\\"b-check_item\\\"><td>10<\\/td><td>Пиво Бад светлое пастериз фильтр 5,0% 0,45л ж\\/б АБ ИнБев Эфес<\\/td><td>85.99<\\/td><td>1<\\/td><td>85.99<\\/td><\\/tr><tr class=\\\"b-check_item\\\"><td>11<\\/td><td>БЗМЖ Сыр Камамбер OKEY Selection 50% 125г<\\/td><td>174.99<\\/td><td>1<\\/td><td>174.99<\\/td><\\/tr><tr class=\\\"b-check_item\\\"><td>12<\\/td><td>Карбонад в\\/к в\\/у 350г Велком<\\/td><td>389.99<\\/td><td>1<\\/td><td>389.99<\\/td><\\/tr><tr class=\\\"b-check_item\\\"><td>13<\\/td><td>Гренки ржаные BEERka Бородинские Чеснок\\/Укроп60г+ соус25г<\\/td><td>47.99<\\/td><td>1<\\/td><td>47.99<\\/td><\\/tr><tr class=\\\"b-check_vblock-first\\\"><td colspan=\\\"3\\\" class=\\\"b-check_itogo\\\">ИТОГО:<\\/td><td><\\/td><td class=\\\"b-check_itogo\\\">1587.07<\\/td><\\/tr><tr class=\\\"b-check_vblock-middle\\\"><td colspan=\\\"3\\\">Наличные<\\/td><td><\\/td><td>0.00<\\/td><\\/tr><tr class=\\\"b-check_vblock-middle\\\"><td colspan=\\\"3\\\">Карта<\\/td><td><\\/td><td>1587.07<\\/td><\\/tr><tr class=\\\"b-check_vblock-middle\\\"><td colspan=\\\"3\\\">НДС итога чека со ставкой 20%<\\/td><td><\\/td><td>218.20<\\/td><\\/tr><tr class=\\\"b-check_vblock-middle\\\"><td colspan=\\\"3\\\">НДС итога чека со ставкой 10%<\\/td><td><\\/td><td>25.27<\\/td><\\/tr><tr class=\\\"b-check_vblock-last\\\"><td colspan=\\\"5\\\">ВИД НАЛОГООБЛОЖЕНИЯ: ОСН<\\/td><\\/tr><tr class=\\\"b-check_vblock-first\\\"><td colspan=\\\"5\\\">РЕГ.НОМЕР ККТ: 0000501830040905    <\\/td><\\/tr><tr class=\\\"b-check_vblock-middle\\\"><td colspan=\\\"5\\\">ЗАВОД. №: <\\/td><\\/tr><tr class=\\\"b-check_vblock-middle\\\"><td colspan=\\\"5\\\">ФН: 7281440701464912<\\/td><\\/tr><tr class=\\\"b-check_vblock-middle\\\"><td colspan=\\\"5\\\">ФД: 81838<\\/td><\\/tr><tr class=\\\"b-check_vblock-middle\\\"><td colspan=\\\"5\\\">ФПД#: 2168372554<\\/td><\\/tr><tr class=\\\"b-check_vblock-last\\\"><td colspan=\\\"5\\\" class=\\\"b-check_center\\\"><img src=\\\"\\/qrcode\\/generate?text=t%3D20250125T2152%26s%3D1587.07%26fn%3D7281440701464912%26i%3D81838%26fp%3D2168372554%26n%3D1\\\" alt=\\\"QR код чека\\\" width=\\\"35%\\\" loading=\\\"lazy\\\" decoding=\\\"async\\\"><\\/td><\\/tr><\\/tbody><\\/table>\"\n" +
//                "  },\n" +
//                "  \"request\": {\n" +
//                "    \"qrurl\": \"\",\n" +
//                "    \"qrfile\": \"\",\n" +
//                "    \"qrraw\": \"t=20250125t2152&s=1587.07&fn=7281440701464912&i=81838&fp=2168372554&n=1\",\n" +
//                "    \"manual\": {\n" +
//                "      \"fn\": \"7281440701464912\",\n" +
//                "      \"fd\": \"81838\",\n" +
//                "      \"fp\": \"2168372554\",\n" +
//                "      \"check_time\": \"20250125t2152\",\n" +
//                "      \"type\": \"1\",\n" +
//                "      \"sum\": \"1587.07\"\n" +
//                "    }\n" +
//                "  }\n" +
//                "}\n";