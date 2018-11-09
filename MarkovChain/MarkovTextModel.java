import java.util.*;

//thanks to datadata
// funct1onal 2018

public class MarkovTextModel
{
		public static class MarkovNode
		{
			public char ch;
			public int Count;
			public int FollowCount;
			public HashMap<Character, MarkovNode> Children;

			public MarkovNode(char c)
			{
				Ch = c;
				Count = 1;
				FollowCount = 0;
				Children = new HashMap<Character, MarkovNode>();
			}

			public final MarkovNode AddChild(char c)
			{
				if (Children == null)
				{
					Children = new HashMap<Character, MarkovNode>();
				}
				++FollowCount;
				MarkovNode child;
				tangible.OutObject<MarkovNode> tempOut_child = new tangible.OutObject<MarkovNode>();
				if (Children.TryGetValue(c, tempOut_child))
				{
					child = tempOut_child.argValue;
					++child.size();
				}
				else
				{
					child = tempOut_child.argValue;
					child = new MarkovNode(c);
					Children.put(c, child);
				}
				return child;
			}
		}

		public static final char StartChar = (char)0xFFFE;
		public static final char StopChar = (char)0xFFFF;

		private MarkovNode Root;
		private int ModelOrder;

		public MarkovTextModel(int order)
		{
			ModelOrder = order;
			Root = new MarkovNode(StartChar);
		}

		public final void AddString(String s)
		{
			// Construct the string that will be added.
			StringBuilder sb = new StringBuilder(s.length() + 2 * (ModelOrder));
			// Order+1 Start characters.
			// The string to add.
			// Order+1 Stop characters.
			sb.append(StartChar, ModelOrder);
			sb.append(s);
			sb.append(StopChar, ModelOrder);

			// Naive method
			for (int iStart = 0; iStart < sb.length(); ++iStart)
			{
				// Get the order 0 node
				MarkovNode parent = Root.AddChild(sb.charAt(iStart));

				// Now add N-grams starting with this node
				for (int i = 1; i <= ModelOrder && i + iStart < sb.length(); ++i)
				{
					MarkovNode child = parent.AddChild(sb.charAt(iStart + i));
					parent = child;
				}
			}
		}

		public final void OutputModel()
		{
			OutputNode(Root, 1, "");
		}

		private void OutputNode(MarkovNode node, int parentCount, String pad)
		{
//TODO: Fix Formatting
			System.out.printf(String.format(("%1$s -- "|%,d|" -- %3$") pad, node.Ch, node.size(), (double)node.size() / parentCount);
			if (node.Children != null)
			{
				int cnt = 0;
				for (Map.Entry<Character, MarkovNode> kvp : node.Children.entrySet())
				{
					OutputNode(kvp.getValue(), node.size(), pad + "  ");
					cnt += kvp.getValue().size();
				}
				if (cnt != node.size())
				{
					System.out.println("ERROR: child count doesn't match.");
				}
			}
		}

		private Random RandomSelector = new Random();

		public final String Generate(int order)
		{
			if (order > ModelOrder)
			{
				throw new RuntimeException("Cannot generate higher order than was built.");
			}
			StringBuilder rslt = new StringBuilder();
			rslt.append(StartChar, order);
			int iStart = 0;
			char ch = StartChar;
			do
			{
				MarkovNode node = Root.Children.get(rslt.charAt(iStart));
				for (int i = 1; i < order; ++i)
				{
					node = node.Children.get(rslt.charAt(i + iStart));
				}
				ch = SelectChildChar(node);
				if (ch != StopChar)
				{
					rslt.append(ch);
				}
				++iStart;
			} while (ch != StopChar);

			// remove start characters from the string
			return rslt.toString().substring(order);
		}

		private char SelectChildChar(MarkovNode node)
		{
			// Generate a random number in the range 0..(node.Count-1)
			int rnd = RandomSelector.nextInt(node.FollowCount);

			// Go through the children to select the node
			int cnt = 0;
			for (Map.Entry<Character, MarkovNode> kvp : node.Children.entrySet())
			{
				cnt += kvp.getValue().size();
				if (cnt > rnd)
				{
					return kvp.getKey();
				}
			}
			throw new RuntimeException("This can't happen!");
		}
}
