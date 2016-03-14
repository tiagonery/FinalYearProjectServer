

package org.jivesoftware.smack.packet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.util.XmlStringBuilder;

/**
025 * Default implementation of the PacketExtension interface. Unless a PacketExtensionProvider
026 * is registered with {@link org.jivesoftware.smack.provider.ProviderManager ProviderManager},
027 * instances of this class will be returned when getting packet extensions.<p>
028 *
029 * This class provides a very simple representation of an XML sub-document. Each element
030 * is a key in a Map with its CDATA being the value. For example, given the following
031 * XML sub-document:
032 *
033 * <pre>
034 * &lt;foo xmlns="http://bar.com"&gt;
035 *     &lt;color&gt;blue&lt;/color&gt;
036 *     &lt;food&gt;pizza&lt;/food&gt;
037 * &lt;/foo&gt;</pre>
038 *
039 * In this case, getValue("color") would return "blue", and getValue("food") would
040 * return "pizza". This parsing mechanism mechanism is very simplistic and will not work
041 * as desired in all cases (for example, if some of the elements have attributes. In those
042 * cases, a custom PacketExtensionProvider should be used.
043 *
044 * @author Matt Tucker
045 */
public class DefaultPacketExtension implements PacketExtension {

    private String elementName;
    private String namespace;
    private Map<String,String> map;

    /**
     * Creates a new generic packet extension.
054     *
055     * @param elementName the name of the element of the XML sub-document.
056     * @param namespace the namespace of the element.
057     */
    public DefaultPacketExtension(String elementName, String namespace) {
        this.elementName = elementName;
        this.namespace = namespace;
    }

     /**
064     * Returns the XML element name of the extension sub-packet root element.
065     *
066     * @return the XML element name of the packet extension.
067     */
    public String getElementName() {
        return elementName;
    }

    /**
073     * Returns the XML namespace of the extension sub-packet root element.
074     *
075     * @return the XML namespace of the packet extension.
076     */
    public String getNamespace() {
        return namespace;
    }

    @Override
    public CharSequence toXML() {
        XmlStringBuilder buf = new XmlStringBuilder();
        buf.halfOpenElement(elementName).xmlnsAttribute(namespace).rightAngelBracket();
        for (String name : getNames()) {
            String value = getValue(name);
            buf.element(name, value);
        }
        buf.closeElement(elementName);
        return buf;
    }

    /**
     * Returns an unmodifiable collection of the names that can be used to get
     * values of the packet extension.
     *
     * @return the names.
     */
    public synchronized Collection<String> getNames() {
        if (map == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(new HashMap<String,String>(map).keySet());
    }

    /**
     * Returns a packet extension value given a name.
     *
     * @param name the name.
     * @return the value.
     */
    public synchronized String getValue(String name) {
        if (map == null) {
            return null;
        }
        return map.get(name);
    }

    /**
     * Sets a packet extension value using the given name.
     *
     * @param name the name.
     * @param value the value.
     */
    public synchronized void setValue(String name, String value) {
        if (map == null) {
            map = new HashMap<String,String>();
        }
        map.put(name, value);
    }
}























































