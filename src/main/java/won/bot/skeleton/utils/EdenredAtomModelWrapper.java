// package won.spoco.raidbot.util;
package won.bot.skeleton.utils;

// import org.apache.jena.datatypes.BaseDatatype;
// import org.apache.jena.datatypes.RDFDatatype;
// import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.RDF;
// import won.protocol.util.DateTimeUtils;
import won.protocol.util.DefaultAtomModelWrapper;
import won.protocol.vocabulary.SCHEMA;
import won.protocol.vocabulary.WON;
import won.protocol.vocabulary.WXCHAT;
// import won.protocol.vocabulary.WONCON;
// import won.protocol.vocabulary.WONMATCH;
// import won.protocol.vocabulary.WXGROUP;

// import java.math.RoundingMode;
import java.net.URI;
// import java.text.DecimalFormat;
// import java.text.DecimalFormatSymbols;
// import java.util.Locale;

import org.apache.jena.rdf.model.Resource;

public class EdenredAtomModelWrapper extends DefaultAtomModelWrapper {
    public EdenredAtomModelWrapper(String atomUri) {// , ContextRaid contextRaid) {
        this(URI.create(atomUri));// , contextRaid);
    }

    public EdenredAtomModelWrapper(URI atomUri) {// }, ContextRaid contextRaid) {
        super(atomUri);
        Resource atom = this.getAtomModel().createResource(atomUri.toString());
        // Resource object = atom.getModel().createResource();
        // object.addProperty(RDF.type, SCHEMA.EVENT);
        // atom.addProperty(SCHEMA.OBJECT, object);
        atom.addProperty(DC.title, "hello world!");
        // Resource res = atom.getModel().createResource();
        // res.addProperty(SCHEMA.TITLE, "hello world!");
        this.addSocket("#socket1", WXCHAT.ChatSocketString);
        this.setDefaultSocket("#socket1");
    }
}