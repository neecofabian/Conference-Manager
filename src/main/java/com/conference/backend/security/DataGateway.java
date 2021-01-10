package com.conference.backend.security;

import java.io.*;

/**
 * A gateway to read from files and write to .ser based on entities of type {@code T}.
 *
 * @param <T> the entity type
 */
public class DataGateway<T> {
    /**
     * Populates the repository map from the file at path filePath
     *
     * @author Lindsey Shorser
     * @author Jonathan Calver
     * @param filePath the path of the data file
     * @throws ClassNotFoundException
     *      if a serializable error occurs between the {@code .ser} file and serial version in the repository
     * @return The {@code T} saved in {@code .ser}
     */
    @SuppressWarnings("unchecked")
    public T readFromFile(String filePath) throws ClassNotFoundException {
        try {
            InputStream file = new FileInputStream(filePath);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            // Deserialize the Manager
            final T t = (T) input.readObject();
            input.close();
            return t;
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * Writes the Manager to file at {@code filePath}
     *
     * @author Lindsey Shorser
     * @author Jonathan Calver
     *
     * @param t the type to save
     * @param filePath the file to write the records to
     * @throws IOException if error reading from file
     */
    public void saveToFile(String filePath, T t) throws IOException {
        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        // Serialize the Manager
        output.writeObject(t);
        output.close();
    }
}
