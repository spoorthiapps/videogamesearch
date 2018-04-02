package Response;


import java.util.List;

public class SearchResponse {

   private String error;
   private String status_code;
   private List<SearchItem> results;

   public String getError() {
      return error;
   }

   public void setError(String error) {
      this.error = error;
   }

   public String getStatus_code() {
      return status_code;
   }

   public void setStatus_code(String status_code) {
      this.status_code = status_code;
   }

   public List<SearchItem> getResults() {
      return results;
   }

   public void setResults(List<SearchItem> results) {
      this.results = results;
   }

}
