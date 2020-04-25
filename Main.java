package encryptdecrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

//97-122
public class Main {
    public static void main(String[] args) {
        String operation = "enc";
        String originalMessage = "";
        boolean parsingErrorOccurred = false;
        boolean dataArgOccured = false;
        boolean inArgOccured = false;
        String filenameIn = "";
        String filenameOut = "";
        String algorithm = "shift";
        int key = 0;


        for (int i = 0; i < args.length; i += 2) {
            if (args[i].equals("-mode")) {
                if (args[i + 1].equals("enc") || args[i + 1].equals("dec")) {
                    operation = args[i + 1];
                } else {
                    parsingErrorOccurred = true;
                }
            } else if (args[i].equals("-key")) {
                try {
                    key = Integer.parseInt(args[i + 1]);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else if (args[i].equals("-data")) {
                dataArgOccured = true;
                originalMessage = args[i + 1];
            } else if (args[i].equals("-in")) {
                inArgOccured = true;
                filenameIn = args[i + 1];
            } else if (args[i].equals("-out")) {
                filenameOut = args[i + 1];
            } else if (args[i].equals("-alg")) {
                algorithm = args[i + 1];
            } else {
                System.out.println("Input error");
            }
        }
        System.out.println(operation);
        System.out.println(key);
        System.out.println(algorithm);


        if (operation.equals("enc")) {

            String encryptedMessage = "";
            if (!determineInput(dataArgOccured, inArgOccured)) {

                if (algorithm.equals("unicode")) {
                    MessageEncoder messageEncoder = new MessageEncoder();
                    messageEncoder.setEncodingMethod(new UnicodeEncoder());
                    encryptedMessage = messageEncoder.encode(originalMessage, key);
                    //encryptedMessage = encrypt(originalMessage, key);
                } else {

                    MessageEncoder messageEncoder = new MessageEncoder();
                    messageEncoder.setEncodingMethod(new ShiftEncoder());
                    encryptedMessage = messageEncoder.encode(originalMessage, key);
                    //encryptedMessage = encrypt(originalMessage, key);
                }

            } else {
                File file = new File(filenameIn);
                try (Scanner fileScanner = new Scanner(file)) {
                    originalMessage = fileScanner.nextLine();

                    //encryptedMessage = encrypt(originalMessage, key);
//                    MessageEncoder messageEncoder = new MessageEncoder();
//                    messageEncoder.setEncodingMethod(new UnicodeEncoder());
//                    encryptedMessage = encrypt(originalMessage, key);
                    if (algorithm.equals("unicode")) {
                        MessageEncoder messageEncoder = new MessageEncoder();
                        messageEncoder.setEncodingMethod(new UnicodeEncoder());
                        encryptedMessage = messageEncoder.encode(originalMessage, key);
                        //encryptedMessage = encrypt(originalMessage, key);
                    } else {
                        System.out.println("entered: " + algorithm);
                        MessageEncoder messageEncoder = new MessageEncoder();
                        messageEncoder.setEncodingMethod(new ShiftEncoder());
                        encryptedMessage = messageEncoder.encode(originalMessage, key);
                        //encryptedMessage = encrypt(originalMessage, key);
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            if (filenameOut.length() == 0) {
                System.out.println(encryptedMessage);
            } else {
                File outputFile = new File(filenameOut);
                try (PrintWriter printWriter = new PrintWriter(outputFile)) {
                    printWriter.print(encryptedMessage);
                } catch (FileNotFoundException e) {
                    System.out.println("Error: " + e.getMessage());
                }
                System.out.println("file done");
                System.out.println(originalMessage);
                System.out.println(encryptedMessage);
            }
        } else if (operation.equals("dec")) {
            String decryptedMessage = "";
            if (!determineInput(dataArgOccured, inArgOccured)) {

                if (algorithm.equals("unicode")) {
                    MessageDecoder messageDecoder = new MessageDecoder();
                    messageDecoder.setDecodingMethod(new UnicodeDecoder());
                    decryptedMessage = messageDecoder.decode(originalMessage, key);
                    //encryptedMessage = encrypt(originalMessage, key);
                } else {

                    MessageDecoder messageDecoder = new MessageDecoder();
                    messageDecoder.setDecodingMethod(new ShiftDecoder());
                    decryptedMessage = messageDecoder.decode(originalMessage, key);
                    //encryptedMessage = encrypt(originalMessage, key);
                }

//                MessageDecoder messageDecoder = new MessageDecoder();
//                messageDecoder.setDecodingMethod(new UnicodeDecoder());
//                decryptedMessage = decrypt(originalMessage, key);

                //decryptedMessage = decrypt(originalMessage, key);
            } else {
                File file = new File(filenameIn);
                try (Scanner fileScanner = new Scanner(file)) {
                    originalMessage = fileScanner.nextLine();

                    if (algorithm.equals("unicode")) {
                        MessageDecoder messageDecoder = new MessageDecoder();
                        messageDecoder.setDecodingMethod(new UnicodeDecoder());
                        decryptedMessage = messageDecoder.decode(originalMessage, key);
                        //encryptedMessage = encrypt(originalMessage, key);
                    } else {

                        MessageDecoder messageDecoder = new MessageDecoder();
                        messageDecoder.setDecodingMethod(new ShiftDecoder());
                        decryptedMessage = messageDecoder.decode(originalMessage, key);
                        //encryptedMessage = encrypt(originalMessage, key);
                        System.out.println(originalMessage);
                        System.out.println(decryptedMessage);
                        System.out.println((int) originalMessage.charAt(0));
                        System.out.println((int) decryptedMessage.charAt(0));
                    }

//                    MessageDecoder messageDecoder = new MessageDecoder();
//                    messageDecoder.setDecodingMethod(new UnicodeDecoder());
//                    decryptedMessage = decrypt(originalMessage, key);

                    //decryptedMessage = decrypt(originalMessage, key);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            if (filenameOut.length() == 0) {
                System.out.println(decryptedMessage);
            } else {
                System.out.println("to file");
                File outputFile = new File(filenameOut);
                try (PrintWriter printWriter = new PrintWriter(outputFile)) {
                    printWriter.print(decryptedMessage);
                } catch (FileNotFoundException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }

    }


    // false - data, true - in
    public static boolean determineInput(boolean data, boolean in) {
        if (data && in)
            return false;
        else if (data && !in)
            return false;
        else if (!data && in) {
            return true;
        } else
            return false;
    }

    public static String encrypt(String originalMessage, int key) {
        String encryptedMessage = "";
        for (int i = 0; i < originalMessage.length(); i++) {
            if (originalMessage.charAt(i) + key > 126) {
                encryptedMessage += (char) (32 + ((int) originalMessage.charAt(i) + key - 1 - 126));
            } else if (originalMessage.charAt(i) + key >= 32 &&
                    originalMessage.charAt(i) + key <= 126) {
                encryptedMessage += (char) ((int) originalMessage.charAt(i) + key);
            } else {
                encryptedMessage += originalMessage.charAt(i);
            }
        }
        return encryptedMessage;
    }

    public static String decrypt(String originalMessage, int key) {
        String decryptedMessage = "";
        for (int i = 0; i < originalMessage.length(); i++) {
            if (originalMessage.charAt(i) - key < 32) {
                decryptedMessage += (char) (126 - ((int) originalMessage.charAt(i) - key - 1 + 32));
            } else if (originalMessage.charAt(i) - key >= 32 &&
                    originalMessage.charAt(i) - key <= 126) {
                decryptedMessage += (char) ((int) originalMessage.charAt(i) - key);
            } else {
                decryptedMessage += originalMessage.charAt(i);
            }
        }
        return decryptedMessage;
    }

}

interface Encoder {
    String encrypt(String originalMessage, int key);
}

class UnicodeEncoder implements Encoder {

    @Override
    public String encrypt(String originalMessage, int key) {
        String encryptedMessage = "";
        for (int i = 0; i < originalMessage.length(); i++) {
            if (originalMessage.charAt(i) + key > 126) {
                encryptedMessage += (char) (32 + ((int) originalMessage.charAt(i) + key - 1 - 126));
            } else if (originalMessage.charAt(i) + key >= 32 &&
                    originalMessage.charAt(i) + key <= 126) {
                encryptedMessage += (char) ((int) originalMessage.charAt(i) + key);
            } else {
                encryptedMessage += originalMessage.charAt(i);
            }
        }
        return encryptedMessage;
    }
}

class ShiftEncoder implements Encoder {

    @Override
    public String encrypt(String originalMessage, int key) {
        String encryptedMessage = "";
        for (int i = 0; i < originalMessage.length(); i++) {
            if (originalMessage.charAt(i) >= 'a' && originalMessage.charAt(i) <= 'z') {
                if (originalMessage.charAt(i) + key > 122) {
                    encryptedMessage += (char) (97 + ((int) originalMessage.charAt(i) + key - 1 - 122));
                } else if (originalMessage.charAt(i) + key >= 97 &&
                        originalMessage.charAt(i) + key <= 122) {
                    encryptedMessage += (char) ((int) originalMessage.charAt(i) + key);
                } else {
                    encryptedMessage += originalMessage.charAt(i);
                }
            } else if (originalMessage.charAt(i) >= 'A' && originalMessage.charAt(i) <= 'Z') {
                if (originalMessage.charAt(i) + key > 90) {
                    encryptedMessage += (char) (65 + ((int) originalMessage.charAt(i) + key - 1 - 90));
                } else if (originalMessage.charAt(i) + key >= 65 &&
                        originalMessage.charAt(i) + key <= 90) {
                    encryptedMessage += (char) ((int) originalMessage.charAt(i) + key);
                } else {
                    encryptedMessage += originalMessage.charAt(i);
                }
            } else if (originalMessage.charAt(i) == ' ') {
                encryptedMessage += originalMessage.charAt(i);
            }
        }
        return encryptedMessage;
    }
}

class MessageEncoder {
    private Encoder encodingMethod;

    public String encode(String originalMessage, int key) {
        return this.encodingMethod.encrypt(originalMessage, key);
    }

    public void setEncodingMethod(Encoder encodingMethod) {
        this.encodingMethod = encodingMethod;
    }
}


interface Decoder {
    String decrypt(String originalMessage, int key);
}

class UnicodeDecoder implements Decoder {

    @Override
    public String decrypt(String originalMessage, int key) {
        String decryptedMessage = "";
        for (int i = 0; i < originalMessage.length(); i++) {
            if (originalMessage.charAt(i) - key < 32) {
                decryptedMessage += (char) (126 - ((int) originalMessage.charAt(i) - key - 1 + 32));
            } else if (originalMessage.charAt(i) - key >= 32 &&
                    originalMessage.charAt(i) - key <= 126) {
                decryptedMessage += (char) ((int) originalMessage.charAt(i) - key);
            } else {
                decryptedMessage += originalMessage.charAt(i);
            }
        }
        return decryptedMessage;
    }
}

class ShiftDecoder implements Decoder {

    @Override//97-122, 65-90
    public String decrypt(String originalMessage, int key) {
        String decryptedMessage = "";
        for (int i = 0; i < originalMessage.length(); i++) {
            if (originalMessage.charAt(i) >= 'a' && originalMessage.charAt(i) <= 'z') {
                if (originalMessage.charAt(i) - key < 97) {
                    decryptedMessage += (char) (122 + (int) originalMessage.charAt(i) - key + 1 - 97);
                } else if (originalMessage.charAt(i) - key >= 97 &&
                        originalMessage.charAt(i) - key <= 122) {
                    decryptedMessage += (char) ((int) originalMessage.charAt(i) - key);
                } else {
                    decryptedMessage += originalMessage.charAt(i);
                }
            } else if (originalMessage.charAt(i) >= 'A' && originalMessage.charAt(i) <= 'Z') {
                if (originalMessage.charAt(i) - key < 65) {
                    decryptedMessage += (char) (90 + (int) originalMessage.charAt(i) - key + 1 - 65);
                } else if (originalMessage.charAt(i) - key >= 65 &&
                        originalMessage.charAt(i) - key <= 90) {
                    decryptedMessage += (char) ((int) originalMessage.charAt(i) - key);
                } else {
                    decryptedMessage += originalMessage.charAt(i);
                }
            } else if (originalMessage.charAt(i) == ' ') {
                decryptedMessage += originalMessage.charAt(i);
            }
        }
        return decryptedMessage;
    }
}

class MessageDecoder {
    private Decoder decodingMethod;

    public String decode(String originalMessage, int key) {
        return this.decodingMethod.decrypt(originalMessage, key);
    }

    public void setDecodingMethod(Decoder decodingMethod) {
        this.decodingMethod = decodingMethod;
    }
}