package org.cloudbus.cloudsim.DLB;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.TensorFlow;
import org.tensorflow.Shape;
import org.tensorflow.*;
import org.tensorflow.types.UInt8;
public class DLBCore {
	String modelDir;
	String graphName;
	byte[] graphDef;
	public static void main(String[] args) throws Exception {
		String libraryDirs = System.getProperty("java.library.path");
		//System.load("/home/zhuxk/eclipse-workspace/TestTestensorflow/lib");
		System.out.print(libraryDirs);
		try (Graph g = new Graph()) {
			final String value = "Hello from " + TensorFlow.version();
			// Construct the computation graph with a single operation, a constant
			// named "MyConst" with a value "value".
			try (Tensor t = Tensor.create(value.getBytes("UTF-8"))) {
				// The Java API doesn't yet include convenience functions for adding operations.
				g.opBuilder("Const", "MyConst").setAttr("dtype", t.dataType()).setAttr("value", t).build();
			}
			// Execute the "MyConst" operation in a Session.
			try (Session s = new Session(g);
					// Generally, there may be multiple output tensors,
					// all of them must be closed to prevent resource leaks.
				Tensor output = s.runner().fetch("MyConst").run().get(0)) {
				//System.out.println(new String(output.bytesValue(), "UTF-8"));
			}
		
		}
		String modelDir = "/home/zhuxk/eclipse-workspace/models/Model4DLB";
		String graphName = "frozen_model.pb";
		/*
		byte[] graphDef = readAllBytesOrExit(Paths.get(modelDir, "frozen_model.pb"));
		try (Tensor<Double> _x = constructAndExecuteBallToTensor(1202.0)) {
			//System.out.print(_x);
			//double[] labelProbabilities = executeModelGraph(graphDef, _x);
			double _pos = executeModelGraph(graphDef, _x);
			System.out.print(_pos);
		}
		*/
		int _pos = GetPos(modelDir, graphName);
		System.out.print(_pos);
	}
	public DLBCore(String modelDir, String graphName) {
		this.graphName = graphName;
		this.modelDir = modelDir;
		this.graphDef = readAllBytesOrExit(Paths.get(modelDir, graphName));
	}
	private static int GetPos(String modelDir, String graphName) {
		byte[] graphDef = readAllBytesOrExit(Paths.get(modelDir, graphName));
		try (Tensor<Double> _x = constructAndExecuteBallToTensor(1202.0)) {
			//System.out.print(_x);
			//double[] labelProbabilities = executeModelGraph(graphDef, _x);
			double _pos = executeModelGraph(graphDef, _x);
			//System.out.print(_pos);
			return (int)_pos;
		}
	}
	public int GetPos(double input) {
		try (Tensor<Double> _x = constructAndExecuteBallToTensor(input)) {
			//System.out.print(_x);
			//double[] labelProbabilities = executeModelGraph(graphDef, _x);
			double _pos = executeModelGraph(this.graphDef, _x);
			//System.out.print(_pos);
			return (int)_pos;
		}
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

	
	
	private static Tensor<Double> constructAndExecuteBallToTensor(double input) {
		try (Graph g = new Graph()) {
			Tensor x = Tensor.create(input, Double.class);
			
			double[][] _input = new double[1][1];
			_input[0][0] = input;
			Tensor _x = Tensor.create(_input);
			
			return _x;
		}
	}
	

	private static Double executeModelGraph(byte[] graphDef, Tensor<Double> input) {
		try (Graph g = new Graph()) {
			g.importGraphDef(graphDef);
			try (Session s = new Session(g);
					// Generally, there may be multiple output tensors, all of them must be closed to prevent resource leaks.
					Tensor result =
							s.runner().feed("hehe/inputs", input).fetch("haha/result").run().get(0)) {
				final long[] rshape = result.shape();
				double _y[][] = new double[1][1];
				result.copyTo(_y);
				return _y[0][0];
			}
		}
	}
}
