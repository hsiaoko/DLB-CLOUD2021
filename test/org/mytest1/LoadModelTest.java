package org.mytest1;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.tensorflow.DataType;
import org.tensorflow.Graph;
import org.tensorflow.Output;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.TensorFlow;
import org.tensorflow.types.UInt8;

public class LoadModelTest {
	private static void printUsage(PrintStream s) {
		final String url = "https://storage.googleapis.com/download.tensorflow.org/models/inception5h.zip";
		s.println(
				"Java program that uses a pre-trained Inception model (http://arxiv.org/abs/1512.00567)");
		s.println("to label JPEG images.");
		s.println("TensorFlow version: " + TensorFlow.version());
		s.println();
		s.println("Usage: label_image <model dir> <image file>");
		s.println();
		s.println("Where:");
		s.println("<model dir> is a directory containing the unzipped contents of the inception model");
		s.println("            (from " + url + ")");
		s.println("<image file> is the path to a JPEG image file");
	}
	public static void main(String[] args) {
		printUsage(System.out);
		String modelDir = "/home/zhuxk/eclipse-workspace/DLB/model/inception5h";
		String imageFile = "";
	    byte[] graphDef = readAllBytesOrExit(Paths.get(modelDir, "tensorflow_inception_graph.pb"));
	    System.out.print(graphDef);
	    List<String> labels =
	    		readAllLinesOrExit(Paths.get(modelDir, "imagenet_comp_graph_label_strings.txt"));
	    System.out.print(labels);
	   // byte[] imageBytes = readAllBytesOrExit(Paths.get(imageFile));
	}
	
	private static byte[] readAllBytesOrExit(Path path) {
		try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			System.err.println("Failed to read [" + path + "]: " + e.getMessage());
			System.exit(1);
		}
		return null;
	}
	private static List<String> readAllLinesOrExit(Path path) {
		try {
			return Files.readAllLines(path, Charset.forName("UTF-8"));
	    } catch (IOException e) {
	    	System.err.println("Failed to read [" + path + "]: " + e.getMessage());
	    	System.exit(0);
	    }
		return null;
	  }
}
