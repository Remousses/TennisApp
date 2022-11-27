import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http'; 

@Injectable({
  providedIn: 'root'
})
export class TennisGameService {
    private backendUrl: string;

    constructor(private httpClient : HttpClient) {
      this.backendUrl = environment.backendUrl + '/game/';
    } 

    startNewGame() {
      return this.httpClient.get(this.backendUrl + 'new-game', {responseType : "text"})
    }
}