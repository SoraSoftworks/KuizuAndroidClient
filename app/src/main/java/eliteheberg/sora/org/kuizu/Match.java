package eliteheberg.sora.org.kuizu;

/**
 * Created by GaSs on 10/12/2015.
 */
public class Match {

    public String idadvers;
    public int score;
    public int scoreadvers;
    public String dateduel;

    public Match(String idadvers, int score, int scoreadvers, String dateduel) {
        this.idadvers = idadvers;
        this.score = score;
        this.scoreadvers = scoreadvers;
        this.dateduel = dateduel;
    }

    public String getIdadvers() {
        return idadvers;
    }

    public void setIdadvers(String idadvers) {
        this.idadvers = idadvers;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScoreadvers() {
        return scoreadvers;
    }

    public void setScoreadvers(int scoreadvers) {
        this.scoreadvers = scoreadvers;
    }

    public String getDateduel() {
        return dateduel;
    }

    public void setDateduel(String dateduel) {
        this.dateduel = dateduel;
    }
}