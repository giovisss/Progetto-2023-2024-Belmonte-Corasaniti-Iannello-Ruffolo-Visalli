import {Injectable} from "@angular/core";
import {Review} from "../model/review";
import {BASE_API_URL} from "../global";
import {HttpClient} from "@angular/common/http";
import {map, Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ReviewService{
  private apiUrl =  BASE_API_URL + '/reviews';

  constructor(private httpClient: HttpClient) {}

  getReview(id: string): Observable<Review> {
    return this.httpClient.get<Review>(`${this.apiUrl}/${id}`);
  }

  insertReview(review: Review): Observable<Review> {
    return this.httpClient.post<Review>(`${this.apiUrl}`, review);
  }

  // updateReview(review: Review): Observable<Review> {
  //   return this.httpClient.put<Review>(`${this.apiUrl}/${review.getId()}`, review);
  // }

  deleteReview(id: string): Observable<Review> {
    return this.httpClient.delete<Review>(`${this.apiUrl}/${id}`);
  }

  getAverageRating(gameId: string): Observable<number> {
    return this.httpClient.get<number>(`${this.apiUrl}/average/${gameId}`);
  }

  getReviewsByGameId(gameId: string): Observable<Review[]> {
    return this.httpClient.get<Review[]>(`${this.apiUrl}/game/${gameId}`);
  }

  getReviewsByUserId(userId: string): Observable<Review[]> {
    return this.httpClient.get<Review[]>(`${this.apiUrl}/user/${userId}`);
  }

  getReviews(): Observable<Review[]> {
    return this.httpClient.get<Review[]>(this.apiUrl);
  }

  getUserReview(gameId: string): Observable<Review> {
    return this.httpClient.get<Review>(`${this.apiUrl}/user_review/${gameId}`);
  }

  getAvgReviews(id: string): Observable<number> {
    return this.httpClient.get<number>(`${this.apiUrl}/average/${id}`);
  }
}
