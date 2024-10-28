import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MTFListTest
{
	private MTFList<Integer> list;
	
	@BeforeEach
	public final void setup()
	{
		list = new MTFList<Integer>();
	}
	
	@Test
	public final void size_0()
	{
		assertEquals(0, list.size(), "size_0 fail");	
	}
	
	@Test
	public final void size_1()
	{
		list.add(1);
		assertEquals(1, list.size(), "size_1 failed");
	}
	
	@Test
	public final void add_1()
	{
		list.add(2);
		list.add(1);
		list.add(0);
		assertEquals("[0, 1, 2]", list.toString(), "insert failed");
	}
	
	@Test
	public final void remove_1()
	{
		list.add(2);
		list.add(1);
		list.add(0);
		list.remove(0);
		assertEquals("[1, 2]", list.toString(), "insert failed");
	}

	@Test
	public final void remove_2()
	{
		list.add(2);
		list.add(1);
		list.add(0);
		list.remove(1);
		assertEquals("[0, 2]", list.toString(), "insert failed");
	}

	@Test
	public final void remove_3()
	{
		list.add(2);
		list.add(1);
		list.add(0);
		list.remove(2);
		assertEquals("[0, 1]", list.toString(), "insert failed");
	}

	@Test
	public final void find_1()
	{
		list.add(2);
		list.add(1);
		list.add(0);
		list.find(0);
		assertEquals("[0, 1, 2]", list.toString(), "insert failed");
	}

	@Test
	public final void find_2()
	{
		list.add(2);
		list.add(1);
		list.add(0);
		list.find(1);
		assertEquals("[1, 0, 2]", list.toString(), "insert failed");
	}

	@Test
	public final void find_3()
	{
		list.add(2);
		list.add(1);
		list.add(0);
		list.find(2);
		assertEquals("[2, 0, 1]", list.toString(), "insert failed");
	}

	@Test
	public final void empty()
	{
		assertEquals(true, list.isEmpty(), "insert failed");
		list.add(2);
		assertEquals(false, list.isEmpty(), "insert failed");
		list.add(1);
		assertEquals(false, list.isEmpty(), "insert failed");
		list.add(0);
		assertEquals(false, list.isEmpty(), "insert failed");
		list.find(2);
		assertEquals(false, list.isEmpty(), "insert failed");
	}
}


