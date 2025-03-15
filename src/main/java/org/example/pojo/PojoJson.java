package org.example.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
