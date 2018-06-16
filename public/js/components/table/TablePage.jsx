// @flow

import React from 'react';
import { List } from 'immutable';
import moment from 'moment';
import { Row, Col, Panel } from 'react-bootstrap';

type Props = {
  rooms: List<Object>,
  fetchRooms: Function,
};

/**
 * TODO: Split into multiple components.
 */
export default class TablePage extends React.Component<Props> {
  constructor(props: Props) {
    super(props);

    this.state = {
      forceRefreshInterval: null,
      forceRefreshDummy: 0,
    };
  }

  componentDidMount(): void {
    const { fetchRooms } = this.props;

    fetchRooms();

    const forceRefreshInterval = setInterval(
      async () => {
        await fetchRooms();
        this.setState({ forceRefreshDummy: Math.random() });
      },
      1000 * 30
    );

    this.setState({ forceRefreshInterval });
  }

  componentWillUnmount(): void {
    const { forceRefreshInterval } = this.state;

    if (forceRefreshInterval) {
      clearInterval(forceRefreshInterval);
    }
  }

  render(): React$Node {
    const { rooms } = this.props;

    return (
      <div>
        <h1>Pokeripöytien tilanne</h1>

        <Row>
          {rooms.map(([roomName, tables]) => (
            <Col sm={4} key={roomName}>
              <Panel style={{ height: 200 }}>
                <Panel.Heading>
                  <Panel.Title>
                    {roomName}
                  </Panel.Title>
                </Panel.Heading>
                <Panel.Body>
                  {tables.length === 0 && (
                    <p>
                      Ei avoimia pöytiä.
                    </p>
                  )}
                  <ul>
                    {tables.map((table, index) => (
                      <li key={index}>
                        {table.count}x {table.stakes ? `${table.stakes[0]}/${table.stakes[1]}` : ''} {table.tableType}
                        {table.interested === 0 && (
                          <span>
                            {table.open === 0 && (
                              <span>
                                {', '}täynnä
                              </span>
                            )}
                            {table.open > 0 && (
                              <span>
                                {', '}{table.open} paikkaa
                              </span>
                            )}
                            {table.open === 0 && (
                              <span>
                                {', '}{table.waiting} jonossa
                              </span>
                            )}
                          </span>
                        )}
                        {table.interested > 0 && (
                          <span>
                            {', '}{table.interested} kiinnostunutta
                          </span>
                        )}
                      </li>
                    ))}
                  </ul>
                  <p style={{ position: 'absolute', bottom: 20 }}>
                    <i className="fa fa-clock-o" /> Päivitetty <b>{moment(tables[0].updatedAt).fromNow()}</b>
                  </p>
                </Panel.Body>
              </Panel>
            </Col>
          ))}
        </Row>
      </div>
    );
  }
}
