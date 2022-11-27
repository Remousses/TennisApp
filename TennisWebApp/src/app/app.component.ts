import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { TennisGameService } from './service/tennis-game.service';
import { TennisGameWebService } from './service/tennis-game.web-service';

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"],
})

export class AppComponent implements OnInit, OnDestroy {

  tennisGameScoreInfo$: Observable<String[]>;
  all: String[];

  constructor(private tennisGameWebService: TennisGameWebService,
              private tennisGameService : TennisGameService) { }
  
  ngOnInit() {
    this.tennisGameWebService.startTennisGameScoreInfoEventSource();
    this.tennisGameScoreInfo$ = this.tennisGameWebService.tennisGameScoreInfoData;
  }

  startNewGame() {
    this.tennisGameService.startNewGame().subscribe({
      next: (res) => {
        console.log(res);
      },
      error: (e) => {
        console.log(e);
      }
    });
  }

  ngOnDestroy() {
    this.tennisGameWebService.onClose();
  }

  @HostListener('window:beforeunload', [ '$event' ])
  unloadHandler(event) {
    console.log('unloadHandler');
    this.tennisGameWebService.onClose();
  }
}
