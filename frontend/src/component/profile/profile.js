

373-16-2 Alpha / prj · Files
CSIL GitLab

    Go to group
    Project
    Activity
    Files
    Commits
    Builds 0
    Graphs
    Milestones
    Issues 25
    Merge Requests 5
    Members
    Labels
    Wiki
    Settings

Profile
pthomsen

    prj frontend src component profile profile.js

    Change text color in profile. Fixes #93


    fb16179f
    Karan Sharma authored 10 days ago
    Browse Files »

profile.js 8.23 KB
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
53
54
55
56
57
58
59
60
61
62
63
64
65
66
67
68
69
70
71
72
73
74
75
76
77
78
79
80
81
82
83
84
85
86
87
88
89
90
91
92
93
94
95
96
97
98
99
100
101
102
103
104
105
106
107
108
109
110
111
112
113
114
115
116
117
118
119
120
121
122
123
124
125
126
127
128
129
130
131
132
133
134
135
136
137
138
139
140
141
142
143
144
145
146
147
148
149
150
151
152
153
154
155
156
157
158
159
160
161
162
163
164
165
166
167
168
169
170
171
172
173
174
175
176
177
178
179
180
181
182
183
184
185
186
187
188
189
190
191
192
193
194
195
196
197
198
199
200
201
202
203
204
205
206
207
208
209
210
211
212
213
214
215
216
217
218
219
220
221
222
223
224
225
226
227
228
229
230
231
232
233
234
235
236
237
238
239
240
241
242
243
244
245
246
247
248
249
250
251
252
253
254
255
256
257
258
259
260
261
262
263
264
265
266
267
268
269
270
271
272
273
274
275
276
277
278
279
280
281
282
283
284
285
286
287
288
289
290
291
292
293
294
295
296
297
298
299
300
301
302
303
304
305
306
307
308
309
310
311
312
313
314
315
316
317
318
319
320
321
322
323
324
325
326
327
328
329
330
331
332
333
334
335

import {createElement, Element} from 'react';
import {FormattedMessage} from 'react-intl';
import {connect} from 'react-redux';
import {reduxForm} from 'redux-form';
import {addTeam, updateTeamStatus} from '../../action/teams';
import {SubmitBtn} from '../button';
import {withRouter} from 'react-router';
import {getTeamInfo} from '../../action/users';

import map from 'lodash/fp/map';
import styles from './profile.css';
import Heading from '../heading/heading';
import classNames from 'classnames';
import isEmpty from 'lodash/fp/isEmpty';
import sortBy from 'lodash/fp/sortBy';

const validate = (values, userInfo) => {
  const errors = {};
  if (!values.secondPlayerId) {
    errors.secondPlayerId = 'Required';
  }
  if (userInfo.userId === values.secondPlayerId) {
    errors.secondPlayerId = 'Cannot be same person';
  }
  return errors;
};
const validateUpdateStatus = (values) => {
  const errors = {};
  if (!values.playTime) {
    errors.playTime = 'Required';
  }
  if (!values.teamId) {
    errors.teamId = 'Required';
  }
  return errors;
};

const playTimes = [{
  time: '8:00 pm',
  value: 'TIME_SLOT_A',
}, {
  time: '9:30 pm',
  value: 'TIME_SLOT_B',
}, {
  time: 'Not Playing',
  value: 'NONE',
}];

const UpdateAttendanceForm = reduxForm({
  form: 'updateTeam',
  fields: ['teamId', 'playTime'],
})(({
  fields: {teamId, playTime},
  teams,
  handleSubmit,
}) => (
  <form
    className={styles.formHorizontal}
    onSubmit={handleSubmit}
  >
  <div className={classNames(styles.formGroup)}>
    <label
      className={classNames(styles.colXsTitle)}
    >
      <FormattedMessage
        id='selectTeam'
        defaultMessage='Select Team'
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: teamId.error &&
                            teamId.touched})}
      {...teamId}
    >
      <option value=''>Select a team...</option>
      {map((teams) => (
        <option value={teams.teamId}key={teams.teamId}>
          With:  {teams.secondPlayer.name}
        </option>
      ), teams)}
    </select>
    {teamId.touched && teamId.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {teamId.error}
          </Heading>
      </div>}
  </div>
  <div className={classNames(styles.formGroup)}>
    <label
      className={classNames(styles.colXsTitle)}
    >
      <FormattedMessage
        id='Select a Time'
        defaultMessage='Time'
      />
    </label>
    <select
      className={classNames(styles.goodForm, {
        [styles.errorForm]: playTime.error &&
                            playTime.touched})}
      {...playTime}
    >
      <option value=''>Select a time...</option>
      {map((playTimes) => (
        <option value={playTimes.value}key={playTimes.value}>
          {playTimes.time}
        </option>
      ), playTimes)}
    </select>
    {playTime.touched && playTime.error &&
      <div className={classNames(styles.errorMsg)}>
      <Heading kind='error'>
            {playTime.error}
          </Heading>
      </div>}
  </div>
    <div className={classNames(styles.center)}>
      <SubmitBtn type='submit'>Update Attendance</SubmitBtn>
    </div>
  </form>
));

const CreateTeamForm = reduxForm({
  form: 'teamCreate',
  fields: ['secondPlayerId'],
  validate,
})(({
  fields: {secondPlayerId},
  players,
  handleSubmit,
}) => (
  <form
    className={styles.formHorizontal}
    onSubmit={handleSubmit}
  >
    <div className={classNames(styles.formGroup)}>
      <label
        className={classNames(styles.colXsTitle)}
      >
        <FormattedMessage
          id='secondPlayerId'
          defaultMessage="Select Team Member's Name"
        />
      </label>
      <select
        className={classNames(styles.goodForm, {
          [styles.errorForm]: secondPlayerId.error &&
                              secondPlayerId.touched})}
        {...secondPlayerId}
      >
        <option value=''>Select a player...</option>
        {map((player) => (
          <option value={player.userId}key={player.userId}>
            {player.firstName} {player.userId}
          </option>
        ), players)}
      </select>
      {secondPlayerId.touched && secondPlayerId.error &&
        <div className={classNames(styles.errorMsg)}>
          <Heading kind='error'>
            {secondPlayerId.error}
          </Heading>
        </div>}
    </div>
    <div className={classNames(styles.center)}>
      <SubmitBtn type='submit'>Create Team</SubmitBtn>
    </div>
  </form>
));

const displayMyInfo = (userInfo) => (
  <div>
    <div className={styles.profileDetailsField}>
      <FormattedMessage
        id='firstName'
        defaultMessage='Name: '
      />
    </div>
    <div className={styles.profileDetails}>
      {userInfo.firstName} {userInfo.lastName}
    </div>
    <br/>
    <div className={styles.profileDetailsField}>
      <FormattedMessage
        id='phoneNumber'
        defaultMessage='Phone Number: '
      />
    </div>
    <div className={styles.profileDetails}>
      {userInfo.phoneNumber}
    </div>
    <br/>
    <div className={styles.profileDetailsField}>
      <FormattedMessage
        id='emailAddress'
        defaultMessage='Email: '
      />
    </div>
    <div className={styles.profileDetails}>
      {userInfo.emailAddress}
    </div>
  </div>
);

const getTime = (time) => {
  if (time === 'TIME_SLOT_A') {
    return '8:30';
  } else if (time === 'TIME_SLOT_B') {
    return '9:30';
  }
  return 'NONE';
};

const displayTeamInfo = map((teamInfo) => (
  <div
    key={teamInfo.teamId}
    className={styles.inactiveProfileTeams}
  >
    <div className={styles.profileDetailsField}>
      <FormattedMessage
        id='firstPlayer'
        defaultMessage='Team With: '
      />
    </div>
    <div className={styles.profileDetails}>
      {teamInfo.secondPlayer.name}
    </div>
    <br/>
    <div className={styles.profileDetailsField}>
      <FormattedMessage
        id='playTime'
        defaultMessage='Preferred Play Time: '
      />
    </div>
    <div className={styles.profileDetails}>
      {getTime(teamInfo.playTime)}
    </div>
  </div>
));

const CreateTeam = withRouter(({
  addTeam,
  players,
  teams,
  router,
  login,
  userInfo,
  teamInfo,
  getTeamInfo,
  updateTeamStatus,
}) : Element => (
  <div className={styles.createTeam}>
      <div className={styles.sectionHeaders}>
        <FormattedMessage
          id='myInfo'
          defaultMessage='My Profile'
        />
      </div>
    {displayMyInfo(userInfo)}
    <div className={styles.sectionHeaders}>
      <FormattedMessage
        id='myTeams'
        defaultMessage='My Teams'
      />
    </div>
    {displayTeamInfo(teamInfo)}
    <div className={styles.sectionHeaders}>
      <FormattedMessage
        id='teamAttendance'
        defaultMessage='Update Attendance'
      />
    </div>
    <UpdateAttendanceForm
      teams={teamInfo}
      userInfo={userInfo}
      onSubmit={(props) => {
        const errors = validateUpdateStatus(props);
        if (!isEmpty(errors)) {
          return Promise.reject(errors);
        }
        return updateTeamStatus({
          ...props,
          authorizationToken: login.authorizationToken,
        }).then(() => {
          getTeamInfo().then(() => {
            router.push('/ladder');
          });
        });
      }}
    />
  <div className={styles.sectionHeaders}>
    <FormattedMessage
      id='createTeam'
      defaultMessage='Create Team'
    />
   </div>
    <CreateTeamForm
      players={players}
      teams={teams}
      userInfo={userInfo}
      onSubmit={(props) => {
        const errors = validate(props, userInfo);
        if (!isEmpty(errors)) {
          return Promise.reject(errors);
        }
        return addTeam({
          ...props,
          firstPlayerId: userInfo.userId,
        }, login).then(() => {
          getTeamInfo().then(() => {
            router.push('/ladder');
          });
        }).catch(() => {
          const errors = {secondPlayerId: 'team exists'};
          return Promise.reject(errors);
        });
      }}
    />
  </div>
));
export default connect(
  (state) => ({
    players: sortBy('firstName', state.app.players),
    teams: state.app.teams,
    login: state.app.loggedIn,
    userInfo: state.app.userInfo,
    teamInfo: state.app.teamInfo,
  }), {
    addTeam,
    displayMyInfo,
    getTeamInfo,
    updateTeamStatus}
)(CreateTeam);

