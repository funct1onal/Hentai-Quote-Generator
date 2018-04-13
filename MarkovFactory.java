import java.util.*;

	public class MarkovFactory
	{
		public static void ReadSonnetsFromFile(MarkovTextModel model, String fname)
		{
			try (InputStreamReader sr = new InputStreamReader(fname))
			{
				StringBuilder sonnet = new StringBuilder();
				String line;
				while ((line = sr.ReadLine()) != null)
				{
					line = line.trim();
					if (line.length() > 3)
					{
						// append the line to the sonnet
						sonnet.append(line + "\r\n");
					}
					else if (sonnet.length() > 0)
					{
						// WriteLine simulates passing a string to the model
						model.AddString(sonnet.toString());
						sonnet.setLength(0);
					}
				}
				if (sonnet.length() > 0)
				{
					model.AddString(sonnet.toString());
				}
			}
		}

		public static void ReadSonnets(MarkovTextModel model, String input)
		{
			try (StringReader sr = new StringReader(input))
			{
				StringBuilder sonnet = new StringBuilder();
				String line;
				while ((line = sr.ReadLine()) != null)
				{
					line = line.trim();
					if (line.length() > 3)
					{
						// append the line to the sonnet
						sonnet.append(line + "\r\n");
					}
					else if (sonnet.length() > 0)
					{
						// WriteLine simulates passing a string to the model
						model.AddString(sonnet.toString());
						sonnet.setLength(0);
					}
				}
				if (sonnet.length() > 0)
				{
					model.AddString(sonnet.toString());
				}
			}
		}
	}