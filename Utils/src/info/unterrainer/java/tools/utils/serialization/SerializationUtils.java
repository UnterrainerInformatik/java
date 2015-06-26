/*
 * Copyright 2012 NTS New Technology Systems GmbH. All Rights reserved. NTS PROPRIETARY/CONFIDENTIAL. Use is subject to
 * NTS License Agreement. Address: Doernbacher Strasse 126, A-4073 Wilhering, Austria Homepage: www.ntswincash.com
 */
package info.unterrainer.java.tools.utils.serialization;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.InputSource;

import info.unterrainer.java.tools.utils.StringUtils;
import info.unterrainer.java.tools.utils.files.Encoding;
import info.unterrainer.java.tools.utils.files.FileUtils;

/**
 * The Class Serializations.
 * <p>
 * Contains utility-methods involving serialization.
 */
@SuppressWarnings({ "unchecked" })
public final class SerializationUtils {

	private static final String ERROR_WORKING_WITH_THE_UNTERLYING_OUTPUT_STREAM_DURING_SERIALIZATION = "Error working with the unterlying output stream during serialization.\n";
	private static final String JAXB_TRANSFORM_INDENT_AMOUNT = "{http://xml.apache.org/xslt}indent-amount";
	private static final String JAXB_TRANSFORM_PROPERTY_YES = "yes";
	private static final String JAXB_TRANSFORM_PROPERTY_NO = "no";

	private static final Logger logger = LogManager.getLogger(SerializationUtils.class);

	/**
	 * Instantiates a new Serializations-class. Here in order to hide the public constructor since this is a static utility class.
	 */
	private SerializationUtils() {
	}

	/**
	 * Serializes a given class (that has to implement Serializable) to a file using built in JaxB XML-serialization. Your file is written 'as-is'. No changes
	 * are made. If you want to pretty-print the output use the {@link #jaxBXmlTransformerSerialize(Object, File, String)} method.
	 * <p>
	 * Be sure to have the source-class annotated accordingly.
	 *
	 * @param serializableObject
	 *            {@link Object} the serializable object
	 * @return the string {@link String} containing the serialized form of the given object.
	 */
	public static String jaxBXmlSerialize(final Object serializableObject) {

		JAXBContext jaxbContext;
		StringWriter sw = null;
		try {
			jaxbContext = JAXBContext.newInstance(serializableObject.getClass());
			Marshaller marsh = jaxbContext.createMarshaller();
			marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			sw = new StringWriter();

			marsh.marshal(serializableObject, sw);
			sw.flush();
			return sw.toString();

		} catch (TransformerFactoryConfigurationError e) {
			logger.fatal("There was an error configuring the transformer-factory.\n" + StringUtils.getStackTrace(e));
		} catch (JAXBException e) {
			logger.fatal("There was an error marshalling the file you wanted to serialize.\n" + StringUtils.getStackTrace(e));
		} finally {
			if (sw != null) {
				try {
					sw.close();
				} catch (IOException e) {
					logger.fatal("There was an error closing the stringWriter after serialization.\n" + StringUtils.getStackTrace(e));
				}
			}
		}
		return null;
	}

	/**
	 * Serializes a given class (that has to implement Serializable) to a file using built in JaxB XML-serialization. Your file is written 'as-is'. No changes
	 * are made. If you want to pretty-print the output use the {@link #jaxBXmlTransformerSerialize(Object, File, String)} method.
	 * <p>
	 * Be sure to have the source-class annotated accordingly.
	 *
	 * @param serializableObject
	 *            {@link Object} the serializable object
	 * @param targetFile
	 *            {@link File} the target file
	 */
	public static void jaxBXmlSerialize(final Object serializableObject, final File targetFile) {

		try {
			FileWriter fw = new FileWriter(targetFile);
			fw.write(jaxBXmlSerialize(serializableObject));
			fw.flush();
			fw.close();

		} catch (IOException e) {
			logger.fatal("There was an error writing the file you wanted to serialize.\n" + StringUtils.getStackTrace(e));
			logger.fatal(e.getMessage());
		}
	}

	/**
	 * Serializes a given class (that has to implement Serializable) to a file using built in JaxB XML-serialization and a XML transformer. The transformer
	 * pretty-prints your XML-output before saving.
	 * <p>
	 * Be sure to have the source-class annotated accordingly.
	 *
	 * @param serializableObject
	 *            {@link Object} the serializable object
	 * @param targetFile
	 *            {@link File} the target file
	 * @param elementsEncapsulatedInCdataTags
	 *            {@link String} a white-space delimited list of the elements whose content you want to be encapsulated in CDATA tags
	 */
	public static void jaxBXmlTransformerSerialize(final Object serializableObject, final File targetFile, final String elementsEncapsulatedInCdataTags) {

		jaxBXmlTransformerSerialize(serializableObject, targetFile, elementsEncapsulatedInCdataTags, 2, false);
	}

	/**
	 * Serializes a given class (that has to implement Serializable) to a file using built in JaxB XML-serialization and a XML transformer. The transformer
	 * pretty-prints your XML-output before saving.
	 * <p>
	 * Be sure to have the source-class annotated accordingly.
	 *
	 * @param serializableObject
	 *            {@link Object} the serializable object
	 * @param elementsEncapsulatedInCdataTags
	 *            {@link String} a white-space delimited list of the elements whose content you want to be encapsulated in CDATA tags
	 * @param indent
	 *            {@link int} the indent. Specify zero if you don't want any
	 * @param omitXmlDeclaration
	 *            {@link boolean} true, if the XML-declaration shall be omitted. False otherwise
	 * @return the string {@link String}
	 */
	public static String jaxBXmlTransformerSerialize(final Object serializableObject, final String elementsEncapsulatedInCdataTags, final int indent,
			final boolean omitXmlDeclaration) {
		Transformer t;
		JAXBContext jaxbContext;
		StringWriter sw1 = null;
		StringWriter sw2 = null;
		try {
			t = TransformerFactory.newInstance().newTransformer();

			if (indent > 0) {
				t.setOutputProperty(OutputKeys.INDENT, JAXB_TRANSFORM_PROPERTY_YES);
				t.setOutputProperty(JAXB_TRANSFORM_INDENT_AMOUNT, String.valueOf(indent));
			}
			if (omitXmlDeclaration) {
				t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, JAXB_TRANSFORM_PROPERTY_YES);
			} else {
				t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, JAXB_TRANSFORM_PROPERTY_NO);
			}
			t.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS, elementsEncapsulatedInCdataTags);

			jaxbContext = JAXBContext.newInstance(serializableObject.getClass());
			Marshaller marsh = jaxbContext.createMarshaller();
			marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			sw1 = new StringWriter();
			marsh.marshal(serializableObject, sw1);
			sw1.flush();
			Source xmlInput = new StreamSource(new StringReader(sw1.toString()));
			sw2 = new StringWriter();
			t.transform(xmlInput, new StreamResult(sw2));
			sw2.flush();
			return sw2.toString();

		} catch (TransformerConfigurationException e) {
			logger.fatal("There was an error configuring the XML transformer.\n" + StringUtils.getStackTrace(e));
		} catch (TransformerFactoryConfigurationError e) {
			logger.fatal("There was an error configuring the transformer-factory.\n" + StringUtils.getStackTrace(e));
		} catch (TransformerException e) {
			logger.fatal("There was an error transforming the file you wanted to serialize.\n" + StringUtils.getStackTrace(e));
		} catch (JAXBException e) {
			logger.fatal("There was an error marshalling the file you wanted to serialize.\n" + StringUtils.getStackTrace(e));
		} finally {
			if (sw1 != null) {
				try {
					sw1.close();
				} catch (IOException e) {
					logger.fatal("There was an error closing the stringWriter after serialization.\n" + StringUtils.getStackTrace(e));
				}
			}
			if (sw2 != null) {
				try {
					sw2.close();
				} catch (IOException e) {
					logger.fatal("There was an error closing the stringWriter after serialization.\n" + StringUtils.getStackTrace(e));
				}
			}
		}
		return null;
	}

	/**
	 * Serializes a given class (that has to implement Serializable) to a file using built in JaxB XML-serialization and a XML transformer. The transformer
	 * pretty-prints your XML-output before saving.
	 * <p>
	 * Be sure to have the source-class annotated accordingly.
	 *
	 * @param serializableObject
	 *            {@link Object} the serializable object
	 * @param targetFile
	 *            {@link File} the target file
	 * @param elementsEncapsulatedInCdataTags
	 *            {@link String} a white-space delimited list of the elements whose content you want to be encapsulated in CDATA tags
	 * @param indent
	 *            {@link int} the indent. Specify zero if you don't want any
	 * @param omitXmlDeclaration
	 *            {@link boolean} true, if the XML-declaration shall be omitted. False otherwise
	 */
	public static void jaxBXmlTransformerSerialize(final Object serializableObject, final File targetFile, final String elementsEncapsulatedInCdataTags,
			final int indent, final boolean omitXmlDeclaration) {

		try {
			FileWriter fw = new FileWriter(targetFile);
			fw.write(jaxBXmlTransformerSerialize(serializableObject, elementsEncapsulatedInCdataTags, indent, omitXmlDeclaration));
			fw.flush();
			fw.close();

		} catch (IOException e) {
			logger.fatal("There was an error writing the file you wanted to serialize.\n" + StringUtils.getStackTrace(e));
			logger.fatal(e.getMessage());
		}
	}

	/**
	 * Deserializes a class from a given source-file that has been serialized using JAXB-XML-serialization.
	 * <p>
	 * Be sure to have the target-class annotated accordingly.<br>
	 * This one should work with most generated XML-files.
	 *
	 * @param <T>
	 *            the generic type
	 * @param source
	 *            {@link String} the source
	 * @param type
	 *            {@link Class<T>} the type
	 * @return the t {@link T}
	 */
	public static <T> T jaxBXmlDeserializer(final String source, final Class<T> type) {
		T result = null;

		try {
			final JAXBContext unmarshallingClassJAXB = JAXBContext.newInstance(type);
			result = (T) unmarshallingClassJAXB.createUnmarshaller().unmarshal(new InputSource(new StringReader(source)));

		} catch (JAXBException e) {
			logger.fatal("There was an error processing the file you wanted to deserialize "
					+ "or when setting up the JAXB context.\n"
					+ StringUtils.getStackTrace(e));
		}

		return result;
	}

	/**
	 * Deserializes a class from a given source-file that has been serialized using JAXB-XML-serialization. Expects the file to be UTF-8.
	 * <p>
	 * Be sure to have the target-class annotated accordingly.<br>
	 * This one should work with most generated XML-files.
	 *
	 * @param <T>
	 *            the generic type
	 * @param sourceFile
	 *            {@link File} the source file
	 * @param type
	 *            {@link Class<T>} the type
	 * @return the t {@link T}
	 */
	public static <T> T jaxBXmlDeserializer(final File sourceFile, final Class<T> type) {

		String source = FileUtils.readFileToString(sourceFile, Encoding.UTF8);
		return jaxBXmlDeserializer(source, type);
	}

	/**
	 * Deserializes a class from a given source-file that has been serialized using JAXB-XML-serialization.
	 * <p>
	 * Be sure to have the target-class annotated accordingly.<br>
	 * This one should work with most generated XML-files.
	 *
	 * @param <T>
	 *            the generic type
	 * @param sourceFile
	 *            {@link File} the source file
	 * @param encoding
	 *            {@link Encoding} the encoding
	 * @param type
	 *            {@link Class<T>} the type
	 * @return the t {@link T}
	 */
	public static <T> T jaxBXmlDeserializer(final File sourceFile, final Encoding encoding, final Class<T> type) {

		String source = FileUtils.readFileToString(sourceFile, encoding);
		return jaxBXmlDeserializer(source, type);
	}

	/**
	 * Serializes a given class (that has to implement Serializable) to a file using java-bean XML-serialization.
	 *
	 * @param serializableObject
	 *            {@link Object} the serializable object
	 * @param targetFile
	 *            {@link File} the target file
	 */
	public static void beansXmlEncode(final Object serializableObject, final File targetFile) {

		XMLEncoder encoder = null;
		try {
			encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(targetFile)));
			encoder.writeObject(serializableObject);

		} catch (FileNotFoundException e) {
			logger.fatal("The file you wanted to serialize to couldn't be" + " opened, created or was a directory.\n" + StringUtils.getStackTrace(e));
		} finally {
			if (encoder != null) {
				encoder.close();
			}
		}
	}

	/**
	 * Deserializes a class from a given source-file that has been serialized using java-bean XML-serialization.
	 *
	 * @param <T>
	 *            the generic type
	 * @param sourceFile
	 *            {@link File} the source file
	 * @param type
	 *            {@link Class<T>} the type
	 * @return the t {@link T}
	 */
	public static <T> T beansXmlDecode(final File sourceFile, final Class<T> type) {

		T result = null;
		XMLDecoder decoder = null;
		try {
			decoder = new XMLDecoder(new FileInputStream(sourceFile));
			result = (T) decoder.readObject();

		} catch (FileNotFoundException e) {
			logger.fatal("The file you wanted to deserialize to couldn't be" + " opened, created or was a directory.\n" + StringUtils.getStackTrace(e));
		} finally {
			if (decoder != null) {
				decoder.close();
			}
		}
		return result;
	}

	/**
	 * Serializes a given class (that has to implement Serializable) to a file using byte-serialization.
	 *
	 * @param serializableObject
	 *            {@link Object} the serializable object
	 * @param targetFile
	 *            {@link File} the target file
	 */
	public static void objectSerialize(final Object serializableObject, final File targetFile) {

		ByteArrayOutputStream baos = SerializationUtils.objectSerialize(serializableObject);

		FileOutputStream outputStream = null;
		try {

			outputStream = new FileOutputStream(targetFile);
			baos.writeTo(outputStream);

		} catch (FileNotFoundException e) {
			logger.fatal("The file you wanted to serialize to couldn't" + " be opened, created or was a directory.\n" + StringUtils.getStackTrace(e));
		} catch (IOException e) {
			logger.fatal(ERROR_WORKING_WITH_THE_UNTERLYING_OUTPUT_STREAM_DURING_SERIALIZATION + StringUtils.getStackTrace(e));
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					logger.fatal("Error closing the output stream during serialization.\n" + StringUtils.getStackTrace(e));
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					logger.fatal("Error closing the output file during serialization.\n" + StringUtils.getStackTrace(e));
				}
			}
		}
	}

	/**
	 * Serializes a given class (that has to implement Serializable) using byte-serialization.
	 *
	 * @param serializableObject
	 *            {@link Object} the serializable object
	 * @return the byte array output stream {@link ByteArrayOutputStream}
	 */
	public static ByteArrayOutputStream objectSerialize(final Object serializableObject) {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(result);
			out.writeObject(serializableObject);
			out.close();
		} catch (IOException e) {
			logger.fatal(ERROR_WORKING_WITH_THE_UNTERLYING_OUTPUT_STREAM_DURING_SERIALIZATION + StringUtils.getStackTrace(e));
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				logger.fatal("Fatal error working with the unterlying output stream during serialization.\n" + StringUtils.getStackTrace(e));
			}
		}
		return result;
	}

	/**
	 * Deserializes a class from a given source-file that has been serialized using byte-serialization and an.
	 *
	 * @param <T>
	 *            the generic type of the class that is contained in the ByteArrayOutputStream
	 * @param sourceFile
	 *            {@link File} the source file to read the object from
	 * @param type
	 *            {@link Class<T>} the type that should be deserialized
	 * @return the t {@link T} the class that was deserialized {@link ObjectOutputStream}.
	 */
	public static <T> T objectDeserialize(final File sourceFile, final Class<T> type) {
		ByteArrayOutputStream baos = null;
		try {
			byte[] ba = FileUtils.readFileToByteArray(sourceFile);
			baos = new ByteArrayOutputStream();
			baos.write(ba);
			baos.flush();
			return SerializationUtils.<T> objectDeserialize(baos, type);
		} catch (IOException e) {
			logger.fatal(ERROR_WORKING_WITH_THE_UNTERLYING_OUTPUT_STREAM_DURING_SERIALIZATION + StringUtils.getStackTrace(e));
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					logger.fatal("Fatal error working with the unterlying" + " output stream during serialization.\n" + StringUtils.getStackTrace(e));
				}
			}
		}
		return null;
	}

	/**
	 * Deserializes a class from a given ByteArrayOutputStream that has been serialized using byte-serialization.
	 *
	 * @param <T>
	 *            the generic type of the class that is contained in the ByteArrayOutputStream
	 * @param baos
	 *            {@link ByteArrayOutputStream} the ByteArrayOutputStream to read the object from
	 * @param type
	 *            {@link Class<T>} the type that should be deserialized
	 * @return the t {@link T} the class that was deserialized
	 */
	public static <T> T objectDeserialize(final ByteArrayOutputStream baos, final Class<T> type) {
		ObjectInputStream in = null;
		T result = null;
		try {
			in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
			result = (T) in.readObject();
		} catch (IOException e) {
			logger.fatal("Error working with the unterlying input stream during serialization.\n" + StringUtils.getStackTrace(e));
		} catch (ClassNotFoundException e) {
			logger.fatal("Error during serialization. The given class could not be found.\n" + StringUtils.getStackTrace(e));
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.fatal("Fatal error working with the unterlying" + " input stream during serialization.\n" + StringUtils.getStackTrace(e));
				}
			}
		}
		return result;
	}

	/**
	 * Creates a partly-deep clone of a serializable data-structure by serializing and deserializing it in one go.
	 * <p>
	 * <b>BEWARE!</b> It's slow and only works for serializable data-structures.
	 *
	 * @param <T>
	 *            the generic type of the handle to the data-structure
	 * @param objectToClone
	 *            {@link T} the data-structure to clone
	 * @return the t {@link T} which now is a perfect clone of the source-data-structure
	 */
	public static <T> T serialClone(final T objectToClone) {
		try {
			return serialCloneInternal(objectToClone);

		} catch (IOException e) {
			logger.fatal(ERROR_WORKING_WITH_THE_UNTERLYING_OUTPUT_STREAM_DURING_SERIALIZATION + StringUtils.getStackTrace(e));
		} catch (ClassNotFoundException e) {
			logger.fatal("Error during serialization. The given class could not be found.\n" + StringUtils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * Internal implementation of serial-clone.
	 *
	 * @param <T>
	 *            the generic type
	 * @param x
	 *            {@link T} the x
	 * @return the t {@link T}
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException
	 *             the ClassNotFoundException
	 */
	private static <T> T serialCloneInternal(final T x) throws IOException, ClassNotFoundException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CloneOutput co = new CloneOutput(baos);
		co.writeObject(x);

		byte[] bytes = baos.toByteArray();
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

		@SuppressWarnings("resource")
		CloneInput cin = new CloneInput(bis, co);

		T clone = (T) cin.readObject();

		return clone;
	}
}