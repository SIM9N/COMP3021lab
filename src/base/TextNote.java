package base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class TextNote extends Note implements java.io.Serializable{

	private static final long serialVersionUID = 9218229230123487366L;
	String content;
	
	public TextNote(String title) {
		super(title);
		this.content = "";
	}
	
	public TextNote(String title, String content) {
		super(title);
		this.content = content;
	}
	
	public TextNote(File f) {
		super(f.getName());
		this.content = getTextFromFile(f.getAbsolutePath());
	}
	
	private String getTextFromFile(String absolutePath) {
		String result = "";
		try {
			FileInputStream fis =  new FileInputStream(absolutePath);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while((line = br.readLine()) != null) {
				result.concat(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void exportTextToFile(String pathFolder) {
		try {
			File file = new File(pathFolder + getTitle().replaceAll(" ", "_") + ".txt");
			FileWriter fw = new FileWriter(file);		
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			file.createNewFile();
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public String getContent() {
		return content;
	}
}
