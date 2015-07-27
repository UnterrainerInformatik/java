/**************************************************************************
 * <pre>
 *
 * Copyright (c) Unterrainer Informatik OG.
 * This source is subject to the Microsoft Public License.
 *
 * See http://www.microsoft.com/opensource/licenses.mspx#Ms-PL.
 * All other rights reserved.
 *
 * (In other words you may copy, use, change and redistribute it without
 * any restrictions except for not suing me because it broke something.)
 *
 * THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR
 * PURPOSE.
 *
 * </pre>
 ***************************************************************************/

package info.unterrainer.java.tools.utils.serialization;

import info.unterrainer.java.tools.utils.files.Encoding;
import info.unterrainer.java.tools.utils.files.FileUtils;

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
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import lombok.Cleanup;
import lombok.experimental.UtilityClass;

import org.xml.sax.InputSource;

@UtilityClass
@SuppressWarnings({ "unchecked" })
public final class SerializationUtils {

	private static final String JAXB_TRANSFORM_INDENT_AMOUNT = "{http://xml.apache.org/xslt}indent-amount";
	private static final String JAXB_TRANSFORM_PROPERTY_YES = "yes";
	private static final String JAXB_TRANSFORM_PROPERTY_NO = "no";

	/**
	 * Serializes a given class (that has to implement Serializable) to a file using built in JaxB XML-serialization. Your file is written 'as-is'. No changes
	 * are made. If you want to pretty-print the output use the {@link #jaxBXmlTransformerSerialize(Object, File, String)} method.
	 * <p>
	 * Be sure to have the source-class annotated accordingly.
	 *
	 * @param serializableObject {@link Object} the serializable object
	 * @return the string {@link String} containing the serialized form of the given object.
	 * @throws JAXBException if an error was encountered while creating the JAXBContext, such as (but not limited to):
	 *             <ol>
	 *             <li>No JAXB implementation was discovered</li> <li>Classes use JAXB annotations incorrectly</li> <li>Classes have colliding annotations
	 *             (i.e., two classes with the same type name)</li> <li>The JAXB implementation was unable to locate provider-specific out-of-band information
	 *             (such as additional files generated at the development time.)</li>
	 *             </ol>
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String jaxBXmlSerialize(final Object serializableObject) throws JAXBException, IOException {

		JAXBContext jaxbContext;
		@Cleanup
		StringWriter sw = null;
		jaxbContext = JAXBContext.newInstance(serializableObject.getClass());
		Marshaller marsh = jaxbContext.createMarshaller();
		marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		sw = new StringWriter();

		marsh.marshal(serializableObject, sw);
		sw.flush();
		return sw.toString();
	}

	/**
	 * Serializes a given class (that has to implement Serializable) to a file using built in JaxB XML-serialization. Your file is written 'as-is'. No changes
	 * are made. If you want to pretty-print the output use the {@link #jaxBXmlTransformerSerialize(Object, File, String)} method.
	 * <p>
	 * Be sure to have the source-class annotated accordingly.
	 *
	 * @param serializableObject {@link Object} the serializable object
	 * @param targetFile {@link File} the target file
	 * @throws JAXBException if an error was encountered while creating the JAXBContext, such as (but not limited to):
	 *             <ol>
	 *             <li>No JAXB implementation was discovered</li> <li>Classes use JAXB annotations incorrectly</li> <li>Classes have colliding annotations
	 *             (i.e., two classes with the same type name)</li> <li>The JAXB implementation was unable to locate provider-specific out-of-band information
	 *             (such as additional files generated at the development time.)</li>
	 *             </ol>
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void jaxBXmlSerialize(final Object serializableObject, final File targetFile) throws JAXBException, IOException {
		FileWriter fw = new FileWriter(targetFile);
		fw.write(jaxBXmlSerialize(serializableObject));
		fw.flush();
		fw.close();
	}

	/**
	 * Serializes a given class (that has to implement Serializable) to a file using built in JaxB XML-serialization and a XML transformer. The transformer
	 * pretty-prints your XML-output before saving.
	 * <p>
	 * Be sure to have the source-class annotated accordingly.
	 *
	 * @param serializableObject {@link Object} the serializable object
	 * @param targetFile {@link File} the target file
	 * @param elementsEncapsulatedInCdataTags {@link String} a white-space delimited list of the elements whose content you want to be encapsulated in CDATA
	 *            tags
	 * @throws JAXBException if an error was encountered while creating the JAXBContext, such as (but not limited to):
	 *             <ol>
	 *             <li>No JAXB implementation was discovered</li> <li>Classes use JAXB annotations incorrectly</li> <li>Classes have colliding annotations
	 *             (i.e., two classes with the same type name)</li> <li>The JAXB implementation was unable to locate provider-specific out-of-band information
	 *             (such as additional files generated at the development time.)</li>
	 *             </ol>
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws TransformerException If an unrecoverable error occurs during the course of the transformation.
	 */
	public static void jaxBXmlTransformerSerialize(final Object serializableObject, final File targetFile, final String elementsEncapsulatedInCdataTags)
			throws JAXBException, IOException, TransformerException {
		jaxBXmlTransformerSerialize(serializableObject, targetFile, elementsEncapsulatedInCdataTags, 2, false);
	}

	/**
	 * Serializes a given class (that has to implement Serializable) to a file using built in JaxB XML-serialization and a XML transformer. The transformer
	 * pretty-prints your XML-output before saving.
	 * <p>
	 * Be sure to have the source-class annotated accordingly.
	 *
	 * @param serializableObject {@link Object} the serializable object
	 * @param elementsEncapsulatedInCdataTags {@link String} a white-space delimited list of the elements whose content you want to be encapsulated in CDATA
	 *            tags
	 * @param indent the indent. Specify zero if you don't want any
	 * @param omitXmlDeclaration true, if the XML-declaration shall be omitted. False otherwise
	 * @return the string {@link String}
	 * @throws JAXBException if an error was encountered while creating the JAXBContext, such as (but not limited to):
	 *             <ol>
	 *             <li>No JAXB implementation was discovered</li> <li>Classes use JAXB annotations incorrectly</li> <li>Classes have colliding annotations
	 *             (i.e., two classes with the same type name)</li> <li>The JAXB implementation was unable to locate provider-specific out-of-band information
	 *             (such as additional files generated at the development time.)</li>
	 *             </ol>
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws TransformerConfigurationException When it is not possible to create a {@link Transformer} instance.
	 * @throws TransformerException If an unrecoverable error occurs during the course of the transformation.
	 */
	public static String jaxBXmlTransformerSerialize(final Object serializableObject, final String elementsEncapsulatedInCdataTags, final int indent,
			final boolean omitXmlDeclaration) throws JAXBException, IOException, TransformerConfigurationException, TransformerException {
		Transformer t;
		JAXBContext jaxbContext;

		@Cleanup
		StringWriter sw1 = null;
		@Cleanup
		StringWriter sw2 = null;

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
	}

	/**
	 * Serializes a given class (that has to implement Serializable) to a file using built in JaxB XML-serialization and a XML transformer. The transformer
	 * pretty-prints your XML-output before saving.
	 * <p>
	 * Be sure to have the source-class annotated accordingly.
	 *
	 * @param serializableObject {@link Object} the serializable object
	 * @param targetFile {@link File} the target file
	 * @param elementsEncapsulatedInCdataTags {@link String} a white-space delimited list of the elements whose content you want to be encapsulated in CDATA
	 *            tags
	 * @param indent the indent. Specify zero if you don't want any
	 * @param omitXmlDeclaration true, if the XML-declaration shall be omitted. False otherwise
	 * @throws JAXBException if an error was encountered while creating the JAXBContext, such as (but not limited to):
	 *             <ol>
	 *             <li>No JAXB implementation was discovered</li> <li>Classes use JAXB annotations incorrectly</li> <li>Classes have colliding annotations
	 *             (i.e., two classes with the same type name)</li> <li>The JAXB implementation was unable to locate provider-specific out-of-band information
	 *             (such as additional files generated at the development time.)</li>
	 *             </ol>
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws TransformerException If an unrecoverable error occurs during the course of the transformation.
	 */
	public static void jaxBXmlTransformerSerialize(final Object serializableObject, final File targetFile, final String elementsEncapsulatedInCdataTags,
			final int indent, final boolean omitXmlDeclaration) throws JAXBException, IOException, TransformerException {
		FileWriter fw = new FileWriter(targetFile);
		fw.write(jaxBXmlTransformerSerialize(serializableObject, elementsEncapsulatedInCdataTags, indent, omitXmlDeclaration));
		fw.flush();
		fw.close();
	}

	/**
	 * Deserializes a class from a given source-file that has been serialized using JAXB-XML-serialization.
	 * <p>
	 * Be sure to have the target-class annotated accordingly.<br>
	 * This one should work with most generated XML-files.
	 *
	 * @param <T> the generic type
	 * @param source {@link String} the source
	 * @param type the type
	 * @return the t
	 * @throws JAXBException if an error was encountered while creating the JAXBContext, such as (but not limited to):
	 *             <ol>
	 *             <li>No JAXB implementation was discovered</li> <li>Classes use JAXB annotations incorrectly</li> <li>Classes have colliding annotations
	 *             (i.e., two classes with the same type name)</li> <li>The JAXB implementation was unable to locate provider-specific out-of-band information
	 *             (such as additional files generated at the development time.)</li>
	 *             </ol>
	 */
	public static <T> T jaxBXmlDeserializer(final String source, final Class<T> type) throws JAXBException {
		T result = null;

		final JAXBContext unmarshallingClassJAXB = JAXBContext.newInstance(type);
		result = (T) unmarshallingClassJAXB.createUnmarshaller().unmarshal(new InputSource(new StringReader(source)));
		return result;
	}

	/**
	 * Deserializes a class from a given source-file that has been serialized using JAXB-XML-serialization. Expects the file to be UTF-8.
	 * <p>
	 * Be sure to have the target-class annotated accordingly.<br>
	 * This one should work with most generated XML-files.
	 *
	 * @param <T> the generic type
	 * @param sourceFile {@link File} the source file
	 * @param type the type
	 * @return the t
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws JAXBException if an error was encountered while creating the JAXBContext, such as (but not limited to):
	 *             <ol>
	 *             <li>No JAXB implementation was discovered</li> <li>Classes use JAXB annotations incorrectly</li> <li>Classes have colliding annotations
	 *             (i.e., two classes with the same type name)</li> <li>The JAXB implementation was unable to locate provider-specific out-of-band information
	 *             (such as additional files generated at the development time.)</li>
	 *             </ol>
	 */
	public static <T> T jaxBXmlDeserializer(final File sourceFile, final Class<T> type) throws IOException, JAXBException {

		String source = FileUtils.readFileToString(sourceFile, Encoding.UTF8);
		return jaxBXmlDeserializer(source, type);
	}

	/**
	 * Deserializes a class from a given source-file that has been serialized using JAXB-XML-serialization.
	 * <p>
	 * Be sure to have the target-class annotated accordingly.<br>
	 * This one should work with most generated XML-files.
	 *
	 * @param <T> the generic type
	 * @param sourceFile {@link File} the source file
	 * @param encoding {@link Encoding} the encoding
	 * @param type the type
	 * @return the t
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws JAXBException if an error was encountered while creating the JAXBContext, such as (but not limited to):
	 *             <ol>
	 *             <li>No JAXB implementation was discovered</li> <li>Classes use JAXB annotations incorrectly</li> <li>Classes have colliding annotations
	 *             (i.e., two classes with the same type name)</li> <li>The JAXB implementation was unable to locate provider-specific out-of-band information
	 *             (such as additional files generated at the development time.)</li>
	 *             </ol>
	 */
	public static <T> T jaxBXmlDeserializer(final File sourceFile, final Encoding encoding, final Class<T> type) throws IOException, JAXBException {

		String source = FileUtils.readFileToString(sourceFile, encoding);
		return jaxBXmlDeserializer(source, type);
	}

	/**
	 * Serializes a given class (that has to implement Serializable) to a file using java-bean XML-serialization.
	 *
	 * @param serializableObject {@link Object} the serializable object
	 * @param targetFile {@link File} the target file
	 * @throws FileNotFoundException if the file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened
	 *             for any other reason
	 */
	public static void beansXmlEncode(final Object serializableObject, final File targetFile) throws FileNotFoundException {

		@Cleanup
		XMLEncoder encoder = null;

		encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(targetFile)));
		encoder.writeObject(serializableObject);
	}

	/**
	 * Deserializes a class from a given source-file that has been serialized using java-bean XML-serialization.
	 *
	 * @param <T> the generic type
	 * @param sourceFile {@link File} the source file
	 * @param type the type
	 * @return the t
	 * @throws FileNotFoundException if the file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened
	 *             for any other reason
	 */
	public static <T> T beansXmlDecode(final File sourceFile, final Class<T> type) throws FileNotFoundException {

		T result = null;
		@Cleanup
		XMLDecoder decoder = null;

		decoder = new XMLDecoder(new FileInputStream(sourceFile));
		result = (T) decoder.readObject();
		return result;
	}

	/**
	 * Serializes a given class (that has to implement Serializable) to a file using byte-serialization.
	 *
	 * @param serializableObject {@link Object} the serializable object
	 * @param targetFile {@link File} the target file
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws FileNotFoundException if the file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened
	 *             for any other reason
	 */
	public static void objectSerialize(final Object serializableObject, final File targetFile) throws IOException, FileNotFoundException {

		@Cleanup
		ByteArrayOutputStream baos = SerializationUtils.objectSerialize(serializableObject);
		@Cleanup
		FileOutputStream outputStream = null;

		outputStream = new FileOutputStream(targetFile);
		baos.writeTo(outputStream);
	}

	/**
	 * Serializes a given class (that has to implement Serializable) using byte-serialization.
	 *
	 * @param serializableObject {@link Object} the serializable object
	 * @return the byte array output stream {@link ByteArrayOutputStream}
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static ByteArrayOutputStream objectSerialize(final Object serializableObject) throws IOException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();

		@Cleanup
		ObjectOutputStream out = new ObjectOutputStream(result);
		out.writeObject(serializableObject);
		out.close();
		return result;
	}

	/**
	 * Deserializes a class from a given source-file that has been serialized using byte-serialization and an.
	 *
	 * @param <T> the generic type of the class that is contained in the ByteArrayOutputStream
	 * @param sourceFile {@link File} the source file to read the object from
	 * @param type the type that should be deserialized
	 * @return the t the class that was deserialized {@link ObjectOutputStream}.
	 * @throws ClassNotFoundException Class of a serialized object cannot be found
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static <T> T objectDeserialize(final File sourceFile, final Class<T> type) throws ClassNotFoundException, IOException {
		@Cleanup
		ByteArrayOutputStream baos = null;

		byte[] ba = FileUtils.readFileToByteArray(sourceFile);
		baos = new ByteArrayOutputStream();
		baos.write(ba);
		baos.flush();
		return SerializationUtils.<T> objectDeserialize(baos, type);
	}

	/**
	 * Deserializes a class from a given ByteArrayOutputStream that has been serialized using byte-serialization.
	 *
	 * @param <T> the generic type of the class that is contained in the ByteArrayOutputStream
	 * @param baos {@link ByteArrayOutputStream} the ByteArrayOutputStream to read the object from
	 * @param type the type that should be deserialized
	 * @return the t the class that was deserialized
	 * @throws IOException if an I/O error occurs while reading the stream header of the input stream
	 * @throws ClassNotFoundException Class of a serialized object cannot be found
	 */
	public static <T> T objectDeserialize(final ByteArrayOutputStream baos, final Class<T> type) throws IOException, ClassNotFoundException {
		@Cleanup
		ObjectInputStream in = null;

		T result = null;
		in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
		result = (T) in.readObject();
		return result;
	}

	/**
	 * Creates a partly-deep clone of a serializable data-structure by serializing and deserializing it in one go.
	 * <p>
	 * <b>BEWARE!</b> It's slow and only works for serializable data-structures.
	 *
	 * @param <T> the generic type of the handle to the data-structure
	 * @param objectToClone the data-structure to clone
	 * @return the t which now is a perfect clone of the source-data-structure
	 * @throws ClassNotFoundException Class of a serialized object cannot be found
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static <T> T serialClone(final T objectToClone) throws ClassNotFoundException, IOException {
		return serialCloneInternal(objectToClone);
	}

	/**
	 * Internal implementation of serial-clone.
	 *
	 * @param <T> the generic type
	 * @param x the x
	 * @return the t
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException Class of a serialized object cannot be found
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