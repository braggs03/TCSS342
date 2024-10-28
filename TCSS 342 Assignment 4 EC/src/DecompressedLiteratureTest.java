import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class DecompressedLiteratureTest
{
	private HuffmanEncoder huffmanEncoder;
	private HuffmanDecoder huffmanDecoder;
	@BeforeEach
	public final void setup() throws IOException {
		huffmanEncoder = new HuffmanEncoder();
		huffmanDecoder = new HuffmanDecoder();
	}
	
	@Test
	public final void compressed_lit() throws Exception
	{
		assertEquals(true, huffmanDecoder.book.length() == 3291642 || huffmanDecoder.book.length() == 3291623, "compressed_lit failed");
		long size = Files.size(Path.of("./WarAndPeace-decompressed.txt"));
		assertEquals(true, size == 3291642 || size == 3291623, "compressed_lit failed");
	}
}


