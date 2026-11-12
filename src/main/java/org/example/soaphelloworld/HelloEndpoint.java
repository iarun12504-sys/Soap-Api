
package org.example.soaphelloworld;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.dom.DOMSource;

@Endpoint
public class HelloEndpoint {

    private static final String NAMESPACE_URI =
            "http://example.com/soap/hello";

    @PayloadRoot(
            namespace = NAMESPACE_URI,
            localPart = "sayHelloRequest"
    )
    @ResponsePayload
    public DOMSource sayHello(@RequestPayload DOMSource request)
            throws ParserConfigurationException {

        // Get the incoming SOAP request node
        Node requestNode = request.getNode();

        // Find the name element
        NodeList nodes;

        if (requestNode instanceof Document) {
            nodes = ((Document) requestNode)
                    .getElementsByTagNameNS(NAMESPACE_URI, "name");
        } else if (requestNode instanceof Element) {
            nodes = ((Element) requestNode)
                    .getElementsByTagNameNS(NAMESPACE_URI, "name");
        } else {
            throw new IllegalArgumentException(
                    "Invalid SOAP request"
            );
        }

        String name = nodes.item(0).getTextContent();

        // Create response XML document
        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .newDocument();

        Element response = document.createElementNS(
                NAMESPACE_URI, "sayHelloResponse"
        );

        Element message = document.createElementNS(
                NAMESPACE_URI, "message"
        );

        message.setTextContent("Hello World " + name);

        response.appendChild(message);
        document.appendChild(response);

        return new DOMSource(document);
    }
}
