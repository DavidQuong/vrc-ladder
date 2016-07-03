import {createElement, Element} from 'react';
import {FormattedMessage} from 'react-intl';
import {connect} from 'react-redux';
// import map from 'lodash/fp/map';
import sortBy from 'lodash/fp/sortBy';
import styles from './match-groups.css';

const MatchGroup = ({
}) : Element => (
  <div className={styles.matchGroupPage}>
    <div className={styles.matchGroupTitle}>
      <FormattedMessage
        id='Matchgroups_title'
        defaultMessage='This weeks&quot; match group'
      />
    </div>
    <div>
      <br/>
      <div className={styles.matchGroup}>
        <div className={styles.matchGroupCourt}>
          <FormattedMessage
            id='court #1'
            defaultMessage='Court #1'
          />
        </div>
          <div className={styles.matchGroupInfo}>
            <FormattedMessage
              id='match1'
              defaultMessage='Team 1 vs Team 2 @8:30pm'
            />
            <div className={styles.matchGroupResultsFORM}>
              <FormattedMessage
                id='matchResults3'
                defaultMessage='Results: <User enter results here (txt, drop?)>'
              />
              </div>
            </div>
            <br/>
          <div className={styles.matchGroupInfo}>
            <FormattedMessage
              id='match2'
              defaultMessage='Team 1 vs Team 3 @8:30pm'
            />
              <div className={styles.matchGroupResultsFORM}>
              <FormattedMessage
                id='matchResults3'
                defaultMessage='Results: <User enter results here (txt, drop?)>'
              />
              </div>
            </div>
          <br/>
            <div className={styles.matchGroupInfo}>
            <FormattedMessage
              id='matchResults3'
              defaultMessage='Team 2 vs Team 3 @8:30pm'
            />
              <div className={styles.matchGroupResultsFORM}>
              <FormattedMessage
                id='matchResults3'
                defaultMessage='Results: <User enter results here (txt, drop?)>'
              />
              </div>
            </div>
          <br/>
      </div>
      <div className={styles.matchGroup}>
        <div className={styles.matchGroupCourt}>
          <FormattedMessage
            id='matchGroup2'
            defaultMessage='Court #2'
          />
        </div>
          <div>
            <div className={styles.matchGroupInfo}>
            <FormattedMessage
              id='match1'
              defaultMessage='Team 4 vs Team 5 @8:30pm'
            />
            <div className={styles.matchGroupResultsFORM}>
              <FormattedMessage
                id='matchResults3'
                defaultMessage='Results: <User enter results here (txt, drop?)>'
              />
              </div>
            </div>
            <br/>
            <div className={styles.matchGroupInfo}>
            <FormattedMessage
              id='match2'
              defaultMessage='Team 4 vs Team 6 @8:30pm'
            />
              <div className={styles.matchGroupResultsFORM}>
              <FormattedMessage
                id='matchResults3'
                defaultMessage='Results: <User enter results here (txt, drop?)>'
              />
              </div>
            </div>
            <br/>
            <div className={styles.matchGroupInfo}>
              <FormattedMessage
                id='match3'
                defaultMessage='Team 5 vs Team 6 @8:30pm'
              />
              <div className={styles.matchGroupResultsFORM}>
              <FormattedMessage
                id='matchResults3'
                defaultMessage='Results: <User enter results here (txt, drop?)>'
              />
              </div>
            </div>
          </div>
          <br/>
      </div>
      <div className={styles.matchGroup}>
        <div className={styles.matchGroupCourt}>
          <FormattedMessage
            id='matchGroup2'
            defaultMessage='Court #2'
          />
        </div>
          <div>
            <div className={styles.matchGroupInfo}>
            <FormattedMessage
              id='match1'
              defaultMessage='Team 7 vs Team 8 @9pm'
            />
            <div className={styles.matchGroupResultsFORM}>
              <FormattedMessage
                id='matchResults3'
                defaultMessage='Results: <User enter results here (txt, drop?)>'
              />
              </div>
            </div>
            <br/>
            <div className={styles.matchGroupInfo}>
            <FormattedMessage
              id='match2'
              defaultMessage='Team 7 vs Team 9 @9pm'
            />
              <div className={styles.matchGroupResultsFORM}>
              <FormattedMessage
                id='matchResults3'
                defaultMessage='Results: <User enter results here (txt, drop?)>'
              />
              </div>
            </div>
            <br/>
            <div className={styles.matchGroupInfo}>
              <FormattedMessage
                id='match3'
                defaultMessage='Team 9 vs Team W @9pm'
              />
              <div className={styles.matchGroupResultsFORM}>
              <FormattedMessage
                id='matchResults3'
                defaultMessage='Results: <User enter results here (txt, drop?)>'
              />
              </div>
            </div>
            <br/>
            <div className={styles.matchGroupInfo}>
              <FormattedMessage
                id='match4'
                defaultMessage='Team 10 vs Team D @9pm'
              />
              <div className={styles.matchGroupResultsFORM}>
              <FormattedMessage
                id='matchResults4'
                defaultMessage='Results: <User enter results here (txt, drop?)>'
              />
              </div>
            </div>
            <br/>
          </div>
          <br/>
      </div>
    </div>
  </div>
);

export default connect(
  (state) => ({
    players: sortBy('firstName', state.app.players),
    teams: sortBy('rank', state.app.teams),
  }),
  {}
)(MatchGroup);
