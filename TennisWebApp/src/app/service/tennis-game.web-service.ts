import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TennisGameWebService {
    private backendUrl: string;
    private eventSource: EventSource;
    private tennisGameScoreDataSource: BehaviorSubject<Array<String>> = new BehaviorSubject([]);

    tennisGameScoreInfoData = this.tennisGameScoreDataSource.asObservable();  

    constructor(private zone: NgZone) {
      this.backendUrl = environment.backendUrl;
    } 

    public startTennisGameScoreInfoEventSource(): void {
      this.eventSource = new EventSource(this.backendUrl + '/flux/score');
      this.eventSource.onmessage = (event) => {

        console.log('got event data', event['data']);
        // JSON.parse(event['data']) if an object 
        const newArrays = [...this.tennisGameScoreDataSource.value, event['data']];

        this.zone.run(() => {
          this.tennisGameScoreDataSource.next(newArrays);
        });
      }

      this.eventSource.onerror = (error) => {
        this.zone.run( () => {
          // readyState === 0 (closed) means the remote source closed the connection,
          // so we can safely treat it as a normal situation. Another way of detecting the end of the stream
          // is to insert a special element in the stream of events, which the client can identify as the last one.
          if(this.eventSource.readyState === 0) {
            this.eventSource.close();
            this.tennisGameScoreDataSource.complete();
          } else {
            this.tennisGameScoreDataSource.error('EventSource error: ' + error);
          }
        });
      }
    }

    public onClose() {
      this.eventSource.close();
      this.tennisGameScoreDataSource.complete();
    }
}