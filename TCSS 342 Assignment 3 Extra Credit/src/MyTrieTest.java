import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MyTrieTest
{
	private MyTrie trie;
	private UniqueWords un;


	@BeforeEach
	public final void setup()
	{
		trie = new MyTrie();
	}
	
	@Test
	public final void size_0()
	{
		assertEquals(0, trie.size(), "size_0 fail");
	}
	
	@Test
	public final void size_1()
	{
		trie.insert("the");
		assertEquals(1, trie.size(), "size_1 failed");
	}
	
	@Test
	public final void insert_1()
	{
		trie.insert("the");
		trie.insert("there");
		trie.insert("their");
		assertEquals("[the, their, there]", trie.toString(), "insert failed");
	}
	
	@Test
	public final void insert_2()
	{
		trie.insert("abc");
		trie.insert("def");
		trie.insert("ghi");
		assertEquals("[abc, def, ghi]", trie.toString(), "insert failed");
	}

	@Test
	public final void insert_3()
	{
		insert_nine_items();
		assertEquals("[The, brown, dog, fox, jumps, lazy, over, quick, the]", trie.toString(), "insert failed");
	}

	@Test
	public final void remove_1()
	{
		insert_nine_items();
		trie.remove("foxes");
		assertEquals("[The, brown, dog, fox, jumps, lazy, over, quick, the]", trie.toString(), "insert failed");
	}

	@Test
	public final void remove_2()
	{
		insert_nine_items();
		trie.remove("fox");
		assertEquals("[The, brown, dog, jumps, lazy, over, quick, the]", trie.toString(), "insert failed");
	}

	@Test
	public final void remove_3()
	{
		trie.insert("the");
		trie.insert("there");
		trie.insert("their");
		trie.remove("the");
		assertEquals("[their, there]", trie.toString(), "insert failed");
		assertEquals(2, trie.size(), "insert failed");
	}

	@Test
	public final void unique_words() throws Exception
	{
		un = new UniqueWords();
		un.addUniqueWordsToTrie();
		assertEquals(true, un.book.book.length() == 3291642 || un.book.book.length() == 3291623, "unique_words failed");
		assertEquals(570240, un.book.words.size(), "unique_words failed");
		assertEquals(20228, un.trieOfUniqueWords.size(), "unique_words failed");
		assertEquals(2650109, un.trieOfUniqueWords.comparisons, "unique_words failed");
	}

	@Test
	public final void find_1()
	{
		insert_nine_items();
		assertEquals(false, trie.find("foxes"), "insert failed");
	}

	@Test
	public final void find_2()
	{
		insert_nine_items();
		assertEquals(true, trie.find("fox"), "insert failed");
	}


	// ---------- helper methods ---------
	
	public final void insert_nine_items()
	{
		String str [] = {"The","quick", "brown","fox","jumps","over","the","lazy","dog"};
		for (int index = 0; index < 9; index++)
			trie.insert(str[index]);
	}
}


