package gvo.util.item;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
public class ItemList {
	@XStreamImplicit(itemFieldName="item")
	 private List<Item> ITEM;  // ITEM类里有多个item

	public List<Item> getITEM() {
		return ITEM;
	}

	public void setITEM(List<Item> iTEM) {
		ITEM = iTEM;
	}

	

	

	

	 
}
