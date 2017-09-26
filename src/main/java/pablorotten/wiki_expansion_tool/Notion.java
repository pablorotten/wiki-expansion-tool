package pablorotten.wiki_expansion_tool;

import java.util.LinkedList;
import java.util.List;

public class Notion implements Comparable<Notion>{
  private String title;
  private NotionType type;
  private Integer pageid;
  private List<String> redirects;
    
  public Notion(String title, NotionType type, Integer pageid) {
    this.title = title;
    this.type = type;
    this.pageid = pageid;
    this.redirects = new LinkedList<String>();
  }
  
  public Notion(String title, NotionType type, Integer pageid, List<String> redirects) {
    this.title = title;
    this.type = type;
    this.pageid = pageid;
    this.redirects = redirects;
  }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public NotionType getType() {
		return type;
	}

	public void setType(NotionType type) {
		this.type = type;
	}

	public Integer getPageid() {
		return pageid;
	}

	public void setPageid(Integer pageid) {
		this.pageid = pageid;
	}

	public List<String> getRedirects() {
		return redirects;
	}

	public void setRedirects(List<String> redirects) {
		this.redirects = redirects;
	}

	public int compareTo(Notion notion) {
		return notion.getPageid().compareTo(this.pageid);		
	}

	@Override
	public String toString() {
		return "Notion [title=" + title + ", type=" + type + ", pageid=" + pageid + ", redirects=" + redirects + "]";
	}

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((pageid == null) ? 0 : pageid.hashCode());
//    result = prime * result + ((redirects == null) ? 0 : redirects.hashCode());
//    result = prime * result + ((title == null) ? 0 : title.hashCode());
//    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Notion other = (Notion) obj;
    if (pageid == null) {
      if (other.pageid != null)
        return false;
    } else if (!pageid.equals(other.pageid))
      return false;
    if (redirects == null) {
      if (other.redirects != null)
        return false;
    } else if (!redirects.equals(other.redirects))
      return false;
    if (title == null) {
      if (other.title != null)
        return false;
    } else if (!title.equals(other.title))
      return false;
    if (type != other.type)
      return false;
    return true;
  }
}
