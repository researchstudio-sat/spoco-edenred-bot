// package won.spoco.raidbot.util;
package won.bot.skeleton.utils;

// import org.apache.jena.datatypes.BaseDatatype;
// import org.apache.jena.datatypes.RDFDatatype;
// import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.RDF;

import won.bot.skeleton.impl.model.EdenredDataPoint;
// import won.protocol.util.DateTimeUtils;
import won.protocol.util.DefaultAtomModelWrapper;
import won.protocol.vocabulary.SCHEMA;
import won.protocol.vocabulary.WON;
import won.protocol.vocabulary.WONCON;
import won.protocol.vocabulary.WONMATCH;
import won.protocol.vocabulary.WXCHAT;
// import won.protocol.vocabulary.WONCON;
// import won.protocol.vocabulary.WONMATCH;
// import won.protocol.vocabulary.WXGROUP;

import java.math.RoundingMode;
// import java.math.RoundingMode;
import java.net.URI;
// import java.text.DecimalFormat;
// import java.text.DecimalFormatSymbols;
// import java.util.Locale;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.apache.jena.datatypes.BaseDatatype;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class EdenredAtomModelWrapper extends DefaultAtomModelWrapper {
    private static Model m = ModelFactory.createDefaultModel(); // TODO or getAtomModel() from DefaultAtomModel
    public static final String BASE_URI = SCHEMA.BASE_URI;
    public static final String DEFAULT_PREFIX = SCHEMA.DEFAULT_PREFIX;
    public static final Property TELEPHONE = m.createProperty(BASE_URI +
                    "telephone");
    public static final Property ADDRESS_COUNTRY = m.createProperty(BASE_URI +
                    "addressCountry");
    public static final Property ADDRESS_LOCALITY = m.createProperty(BASE_URI +
                    "addressLocality");
    public static final Property ADDRESS_REGION = m.createProperty(BASE_URI +
                    "addressRegion");
    public static final Property POSTAL_CODE = m.createProperty(BASE_URI +
                    "postalCode");
    public static final Property STREET_ADRESS = m.createProperty(BASE_URI +
                    "streetAddress");
    public static final Property ADDRESS = m.createProperty(BASE_URI +
                    "address");
    public static final Property GEO_SPATIAL = m.createProperty("https://w3id.org/won/content#geoSpatial");
    public static final Resource FOOD_ESTABLISHMENT = m.createResource(BASE_URI + "FoodEstablishment");

    public EdenredAtomModelWrapper(String atomUri, EdenredDataPoint datapoint) {// , ContextRaid contextRaid) {
        this(URI.create(atomUri), datapoint);// , contextRaid);
    }

    public EdenredAtomModelWrapper(URI atomUri, EdenredDataPoint datapoint) {// }, ContextRaid contextRaid) {
        super(atomUri);
        Resource atom = this.getAtomModel().createResource(atomUri.toString());
        // object.addProperty(RDF.type, SCHEMA.EVENT);
        // atom.addProperty(SCHEMA.OBJECT, object);
        atom.addProperty(RDF.type, WON.Atom);
        atom.addProperty(DC.title, datapoint.getName());
        // move this to a s:location? or have multityping?
        atom.addProperty(RDF.type, FOOD_ESTABLISHMENT);
        atom.addProperty(TELEPHONE, "+43 661 23 45 678");
        atom.addProperty(SCHEMA.URL, "https://example.org/");
        String onelineAddress = "Thurngasse 1, 1090 Wien, AT";
        Resource locationObject = atom.getModel().createResource();
        locationObject.addProperty(RDF.type, SCHEMA.PLACE);
        locationObject.addProperty(SCHEMA.NAME, onelineAddress);
        Resource geoObject = locationObject.getModel().createResource();
        geoObject.addProperty(RDF.type, SCHEMA.GEOCOORDINATES);
        // ---
        DecimalFormat df = new DecimalFormat("##.######");
        df.setRoundingMode(RoundingMode.HALF_UP);
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        String lat = df.format(48.21831);
        String lon = df.format(16.358780);
        geoObject.addProperty(SCHEMA.LATITUDE, lat);
        geoObject.addProperty(SCHEMA.LONGITUDE, lon);
        RDFDatatype bigdata_geoSpatialDatatype = new BaseDatatype(
                        "http://www.bigdata.com/rdf/geospatial/literals/v1#lat-lon");
        geoObject.addProperty(WONCON.geoSpatial, lat + "#" + lon, bigdata_geoSpatialDatatype);
        locationObject.addProperty(SCHEMA.GEO, geoObject);
        atom.addProperty(SCHEMA.LOCATION, locationObject);
        // ---
        this.addTag("Edenred");
        this.addFlag(WONMATCH.NoHintForMe);
        // ---
        // TODO move new properties and literals to schema class
        // TODO number
        // ---
        this.addSocket("#socket1", WXCHAT.ChatSocketString);
        this.setDefaultSocket("#socket1");
        // Resource postalObject = atom.getModel().createResource();
        // postalObject.addProperty(ADDRESS_COUNTRY, "AT");
        // postalObject.addProperty(ADDRESS_LOCALITY, "Wien");
        // postalObject.addProperty(POSTAL_CODE, "1090");
        // postalObject.addProperty(STREET_ADRESS, "Thurngasse 1");
        // atom.addProperty(ADDRESS, onelineAddress);
    }
}