/*
 * ***********************************************************
 * Created by Dmitry I. Gornev  29 апр. 2022
 * dig@inock.ru  https://t.me/inock
 * **********************************************************
 */

// https://coderlessons.com/articles/java/uchebnoe-posobie-po-jaxb-dlia-java-xml-binding-ultimate-guide-pdf-download?
//https://devcolibri.com/jaxb-%D0%B2%D0%B2%D0%B5%D0%B4%D0%B5%D0%BD%D0%B8%D0%B5/?

package ru.inock.webServletResime.storage;

import ru.inock.webServletResime.exception.StorageException;
import ru.inock.webServletResime.model.*;
import ru.inock.webServletResime.storage.storageInterfaces.StreamSerializable;
import jakarta.xml.bind.*;
import java.io.*;
import java.nio.charset.StandardCharsets;


public class XmlJaxbParser implements StreamSerializable {

    public XmlJaxbParser() {
    }

    public String getFileExc() {
        return ".xml";
    }

    JAXBContext context;

    {
        try {
            context = JAXBContext.newInstance(Resume.class, Organisation.class, Organisation.Link.class,
                    OrganisationSection.class, TextSection.class, ListSection.class, Organisation.WorkPeriod.class);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doWrite(OutputStream os, Resume r) {
        //     JAXB.marshal(r, os);
        try(Writer writer = new OutputStreamWriter(os, StandardCharsets.UTF_8);) {

            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            marshaller.marshal(r, writer);
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Resume doReade(InputStream is) {
        //Resume r =  JAXB.unmarshal(is,Resume.class);
        try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);){

            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (Resume) unmarshaller.unmarshal(reader);
        } catch (JAXBException | IOException e) {
            throw new StorageException("Ошибка преобразования формата xml к Resume. ", e);
        }

    }
}





